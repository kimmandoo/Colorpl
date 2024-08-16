package com.presentation.ticket

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketEditBinding
import com.domain.model.TicketRequest
import com.naver.maps.geometry.LatLng
import com.presentation.base.BaseDialogFragment
import com.presentation.component.dialog.TicketCreateDialog
import com.presentation.notification.FcmWorker
import com.presentation.util.ImageProcessingUtil
import com.presentation.util.TicketState
import com.presentation.util.TicketType
import com.presentation.util.formatIsoToPattern
import com.presentation.util.getPhotoGallery
import com.presentation.util.hourToMills
import com.presentation.util.requestCameraPermission
import com.presentation.util.setCameraLauncher
import com.presentation.util.setImageCenterCrop
import com.presentation.util.setImageLauncher
import com.presentation.util.stringToCalendar
import com.presentation.viewmodel.TicketViewModel
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
class TicketEditFragment :
    BaseDialogFragment<FragmentTicketEditBinding>(R.layout.fragment_ticket_edit) {

    private val ticketViewModel: TicketViewModel by hiltNavGraphViewModels(R.id.nav_ticket_update_graph)
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var photoUri: Uri
    private val args: TicketEditFragmentArgs by navArgs()

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                dismissLoading()
                navigatePopBackStack()
            }
        }

    override fun initView(savedInstanceState: Bundle?) {
        initUi()
        observeDescription()
        observeSingleTicket()
        observeTicketEditResponse()
        observeJuso()
        observeDialog()
        initGalleryPhoto()
        initCamera()
    }

    private fun observeSingleTicket() {
        ticketViewModel.ticketData.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            dismissLoading()
            val ticket = it
            binding.apply {
                etTitle.setText(ticket.name)
                ivPoster.setImageCenterCrop(ticket.imgUrl)
                tvConfirm.text = "수정하기"
                tvConfirm.isSelected = true
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeDescription() {
        ticketViewModel.ticketData.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { ticket ->
                dismissLoading()
                ticketViewModel.setLatLng(LatLng(ticket.latitude, ticket.longitude))
                ticketViewModel.setCategory(ticket.category)
                binding.apply {
                    etSeat.setText(ticket.name)
                    ivPoster.setImageCenterCrop(ticket.imgUrl)
                    etDetail.setText(ticket.location)
                    etSeat.setText(ticket.seat)
                    val items = resources.getStringArray(R.array.ticket_category_en)
                    spinner.setSelection(items.indexOf(ticket.category) + 1)
                    tvSchedule.text = ticket.dateTime.formatIsoToPattern()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initUi() {
        ticketViewModel.getSingleTicket(args.ticket)
        binding.tvConfirm.setOnClickListener {
            Timber.d("confirm clicked")
            val file = if (::photoUri.isInitialized) {
                ImageProcessingUtil(binding.root.context).uriToCompressedFile(photoUri)!!
            } else {
                null
            }
            ticketViewModel.editSingleTicket(
                args.ticket,
                file,
                TicketRequest(
                    seat = binding.etSeat.text.toString(),
                    dateTime = binding.tvSchedule.text.toString(),
                    name = binding.etTitle.text.toString(),
                    category = ticketViewModel.category.value,
                    location = binding.etDetail.text.toString(),
                    latitude = ticketViewModel.geocodingLatLng.value.latitude,
                    longitude = ticketViewModel.geocodingLatLng.value.longitude
                )
            )
            showLoading()
        }

        binding.ivPoster.setOnClickListener {
            val dialog = TicketCreateDialog(
                requireContext(),
                type = TicketState.UNISSUED,
                action = { ticketType ->
                    when (ticketType) {
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
                }
            )
            dialog.show()
        }


        binding.cbFindRoute.setOnClickListener {
            showLoading()
            val action =
                TicketEditFragmentDirections.actionTicketEditFragmentToDialogTicketAddress(1)
            findNavController().navigate(action)
        }

        binding.tvSchedule.setOnClickListener {
            showDatePicker()
        }

        val hint = "카테고리를 선택하세요"
        val items = resources.getStringArray(R.array.ticket_category)
        val adapter =
            ArrayAdapter(requireContext(), R.layout.item_category_spinner, listOf(hint) + items)

        binding.spinner.apply {
            this.adapter = adapter
            Timber.tag("spinner").d("${ticketViewModel.category.value}")
            setSelection(items.indexOf(ticketViewModel.category.value) + 1) // +1은 힌트 항목 때문
            adapter.setDropDownViewResource(R.layout.item_category_spinner)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long,
                ) {
                    if (position > 0) {
                        val selectedCategory = items[position - 1]
                        ticketViewModel.setCategory(selectedCategory)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    ticketViewModel.setCategory(ticketViewModel.category.value)
                }
            }
        }

        ticketViewModel.geocodingLatLng.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { latlng ->
                Timber.d("$latlng")
                if (latlng.latitude != 0.0 && latlng.longitude != 0.0) {
                    dismissLoading()
                    binding.cbFindRoute.text = "길찾기를 제공합니다"
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeTicketEditResponse() {
        ticketViewModel.ticketUpdateResponse.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { success ->
                dismissLoading()
                if (success > 0) {
                    Timber.tag("result").d("$success")
                    if (ticketViewModel.geocodingLatLng.value.latitude != 0.0 || ticketViewModel.geocodingLatLng.value.longitude != 0.0) {
                        initWorkerManager()
                    }
                    Toast.makeText(requireContext(), "티켓을 수정했습니다", Toast.LENGTH_SHORT).show()
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("closed", true)
                    navigatePopBackStack()
                } else {
                    Toast.makeText(requireContext(), "티켓 수정에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initWorkerManager() {
        val ticketDate =
            stringToCalendar(binding.tvSchedule.text.toString() ?: "")?.timeInMillis ?: 0
        val currentTime = System.currentTimeMillis()
        val delay = ticketDate - hourToMills(4) - currentTime
        val latLng = ticketViewModel.geocodingLatLng.value
        val data = Data.Builder()
            .putString("latLng", "${latLng.latitude},${latLng.longitude}")
            .build()
        Timber.d("지도 데이터 확인 ${ticketViewModel.geocodingLatLng.value}")
        val fcmWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<FcmWorker>()
            .setInputData(data)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        val workManager = WorkManager.getInstance(requireActivity())
        workManager.enqueue(fcmWorkRequest)
    }

    private fun initGalleryPhoto() {
        pickImageLauncher = setImageLauncher { uri ->
            photoUri = uri
            binding.ivPoster.setImageCenterCrop(photoUri.toString())
        }
    }

    private fun initCamera() {
        takePicture = setCameraLauncher(
            onSuccess = {
                if (::photoUri.isInitialized) {
                    binding.ivPoster.setImageCenterCrop(photoUri.toString())
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
            ticketViewModel.juso.collectLatest { juso ->
                binding.tvHintJuso.text = juso
                binding.tvHintJuso.visibility = if (juso.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("closed", true)
        super.onDismiss(dialog)
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
            ticketViewModel.setSchedule(selectedDateTime)
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
                        ticketViewModel.getSingleTicket(args.ticket)
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
}