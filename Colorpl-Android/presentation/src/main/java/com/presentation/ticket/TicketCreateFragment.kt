package com.presentation.ticket

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketCreateBinding
import com.presentation.base.BaseFragment
import com.presentation.notification.FcmWorker
import com.presentation.util.ImageProcessingUtil
import com.presentation.util.TicketType
import com.presentation.util.getPhotoGallery
import com.presentation.util.hourToMills
import com.presentation.util.requestCameraPermission
import com.presentation.util.setCameraLauncher
import com.presentation.util.setImageLauncher
import com.presentation.util.stringToCalendar
import com.presentation.viewmodel.TicketCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TicketCreateFragment :
    BaseFragment<FragmentTicketCreateBinding>(R.layout.fragment_ticket_create) {

    private val viewModel: TicketCreateViewModel by hiltNavGraphViewModels(R.id.nav_ticket_graph)
    private val args: TicketCreateFragmentArgs by navArgs()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var photoUri: Uri

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                dismissLoading()
                navigatePopBackStack()
            }
        }

    override fun initView() {
        observeDescription()
        observeTicketResponse()
        observeJuso()
        observeDialog()
        initGalleryPhoto()
        initCamera()
        initUi()
    }

    private fun initUi() {
        when (args.photoType) {
            TicketType.CAMERA_UNISSUED -> {
                requireContext().requestCameraPermission(
                    onGrant = {
                        openCamera()
                    },
                    onDenied = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                )
            }

            TicketType.GALLERY_UNISSUED -> {
                getPhotoGallery(pickImageLauncher)
            }
        }

        binding.cbFindRoute.setOnClickListener {
            showLoading()
            val action = TicketCreateFragmentDirections.actionFragmentTicketCreateToDialogTicketAddress(0)
            findNavController().navigate(action)
        }

        binding.tvSchedule.setOnClickListener {
            showDatePicker()
        }

        binding.tvConfirm.setOnClickListener {
            if (viewModel.ticketInfo.value) {
                viewModel.updateSchedule(binding.tvSchedule.text.toString())
                viewModel.setSeat(binding.etSeat.text.toString())
                viewModel.setTitle(binding.etTitle.text.toString())
                viewModel.setLocation(binding.etDetail.text.toString())
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.createTicket(
                        ImageProcessingUtil(binding.root.context).uriToCompressedFile(photoUri)!!,
                    )
                    binding.tvConfirm.isEnabled = false
                    showLoading()
                }
            }
        }
        val hint = "카테고리를 선택하세요"
        val items = resources.getStringArray(R.array.ticket_category)
        val adapter =
            ArrayAdapter(requireContext(), R.layout.item_category_spinner, listOf(hint) + items)

        binding.spinner.apply {
            this.adapter = adapter
            setSelection(0)
            adapter.setDropDownViewResource(R.layout.item_category_spinner)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long,
                ) {
                    if (position > 0) {
                        // 실제 카테고리가 선택된 경우
                        val selectedCategory = items[position - 1]
                        viewModel.setCategory(selectedCategory)
                    } else {
                        viewModel.clearCategory()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    val defaultCategory = items.last()
                    viewModel.setCategory(defaultCategory)
                }
            }
        }
    }

    private fun observeDescription() {
        viewLifecycleOwner.lifecycleScope.launch {
            showLoading()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.description.collectLatest { data ->
                    data?.let {
                        dismissLoading()
                        Timber.tag("description").d(data.toString())
                        binding.apply {
                            etTitle.setText(data.title)
                            etDetail.setText(data.detail)
                            etSeat.setText(data.seat)
                            if (viewModel.scheduleInfo.value) {
                                tvSchedule.text = data.schedule
                            } else {
                                tvSchedule.text = "일정을 선택해주세요"
                            }
                        }
                    }
                }
            }
        }

        viewModel.ticketInfo.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { state ->
            binding.tvConfirm.isSelected = state
            binding.tvConfirm.isEnabled = state
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.category.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { state ->
            binding.tvConfirm.isSelected = state != ""
            binding.tvConfirm.isEnabled = state != ""
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.geocodingLatLng.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { latlng ->
            Timber.d("$latlng")
            if (latlng.latitude != 0.0 && latlng.longitude != 0.0) {
                dismissLoading()
                binding.cbFindRoute.text = "길찾기를 제공합니다"
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeTicketResponse() {
        viewModel.createResponse.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { ticketId ->
                when {
                    ticketId >= 0 -> {
                        if (viewModel.geocodingLatLng.value.latitude != 0.0 || viewModel.geocodingLatLng.value.longitude != 0.0) {
                            initWorkerManager()
                        }
                        dismissLoading()
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("closed", true)
                        Toast.makeText(requireContext(), "티켓을 생성했습니다", Toast.LENGTH_SHORT).show()
                        navigatePopBackStack()
                    }

                    ticketId < 0 -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "티켓 생성에 실패했습니다", Toast.LENGTH_SHORT).show()
                        binding.tvConfirm.isEnabled = true
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initWorkerManager() {
        val description = viewModel.description.value
        val ticketDate = stringToCalendar(description?.schedule ?: "")?.timeInMillis ?: 0
        val currentTime = System.currentTimeMillis()
        val delay = ticketDate - hourToMills(4) - currentTime
        val latLng = viewModel.geocodingLatLng.value
        val data = Data.Builder()
            .putString("latLng", "${latLng.latitude},${latLng.longitude}")
            .build()
        Timber.d("지도 데이터 확인 ${viewModel.geocodingLatLng.value}")
        val fcmWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<FcmWorker>()
            .setInputData(data)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        val workManager = WorkManager.getInstance(requireActivity())
        workManager.enqueue(fcmWorkRequest)
    }

    private fun describeImage(uri: Uri) {
        ImageProcessingUtil(requireContext()).uriToBase64(uri)?.let { base64String ->
            viewModel.getDescription(base64String)
        }
        Glide.with(binding.root.context).load(uri.toString()).centerCrop().into(binding.ivPoster)
    }

    private fun initGalleryPhoto() {
        pickImageLauncher = setImageLauncher { uri ->
            photoUri = uri
            describeImage(uri)
        }
    }

    private fun initCamera() {
        takePicture = setCameraLauncher(
            onSuccess = {
                if (::photoUri.isInitialized) {
                    describeImage(photoUri)
                }
            },
            onFailure = {
                dismissLoading()
                navigatePopBackStack()
            }
        )
    }

    private fun openCamera() {
        val photoFile = File.createTempFile("photo", ".jpg", requireContext().externalCacheDir)
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )
        takePicture.launch(photoUri)
    }

    private fun observeJuso() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.juso.collectLatest { juso ->
                binding.tvHintJuso.text = juso
                binding.tvHintJuso.visibility = if (juso.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun showDatePicker() {
        val currentDateTime = Calendar.getInstance()
        val selectedDateString = binding.tvSchedule.text.toString()
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.getDefault())
        val calendar = if (selectedDateString.isNotEmpty() && selectedDateString != "일정을 선택해주세요") {
            try {
                Calendar.getInstance().apply {
                    time = dateFormat.parse(selectedDateString) ?: Date()
                }
            } catch (e: Exception) {
                currentDateTime
            }
        } else {
            currentDateTime
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            showTimePicker(selectedDate)
        }, year, month, day).show()
    }

    private fun showTimePicker(dateCalendar: Calendar) {
        val currentTime = Calendar.getInstance()
        val selectedDateString = binding.tvSchedule.text.toString()
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.getDefault())

        val calendar = if (selectedDateString.isNotEmpty() && selectedDateString != "일정을 선택해주세요") {
            try {
                Calendar.getInstance().apply {
                    time = dateFormat.parse(selectedDateString) ?: Date()
                }
            } catch (e: Exception) {
                currentTime
            }
        } else {
            currentTime
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            dateCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            dateCalendar.set(Calendar.MINUTE, selectedMinute)
            val selectedDateTime = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.getDefault())
                .format(dateCalendar.time)
            binding.tvSchedule.text = selectedDateTime
        }, hour, minute, false).show()
    }

    private fun observeDialog() {
        viewLifecycleOwner.lifecycleScope.launch {
            findNavController()
                .currentBackStackEntry
                ?.savedStateHandle
                ?.getStateFlow<Boolean>("closed", false)
                ?.collectLatest { closed ->
                    if (closed) {
                        dismissLoading()
                        findNavController().currentBackStackEntry?.savedStateHandle?.set(
                            "closed",
                            false
                        )
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        findNavController().previousBackStackEntry?.savedStateHandle?.set("closed", true)
    }
}