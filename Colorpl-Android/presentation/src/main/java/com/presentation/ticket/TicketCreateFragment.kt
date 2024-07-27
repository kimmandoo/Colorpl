package com.presentation.ticket

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketCreateBinding
import com.domain.model.Description
import com.domain.model.Ticket
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.presentation.base.BaseFragment
import com.presentation.util.ImageProcessingUtil
import com.presentation.util.TicketType
import com.presentation.util.checkCameraPermission
import com.presentation.util.getPhotoGallery
import com.presentation.viewmodel.TicketCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class TicketCreateFragment :
    BaseFragment<FragmentTicketCreateBinding>(R.layout.fragment_ticket_create) {

    private val viewModel: TicketCreateViewModel by hiltNavGraphViewModels(R.id.nav_ticket_graph)
    private val args: TicketCreateFragmentArgs by navArgs()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var photoUri: Uri

    override fun initView() {
//        observeDescription()
        initGalleryPhoto()
        initCamera()
        initUi()
    }

    private fun initUi() {
        when (args.photoType) {
            TicketType.CAMERA -> {
                checkCameraPermission {
                    openCamera()
                }
            }

            TicketType.GALLERY -> {
                getPhotoGallery(pickImageLauncher)
            }
        }
        binding.tvConfirm.setOnClickListener {
            viewModel.setTicketInfo(
                Description(
                    title = binding.etTitle.text.toString(),
                    detail = binding.etDetail.text.toString(),
                    schedule = binding.etSchedule.text.toString(),
                    seat = binding.etSeat.text.toString()
                )
            )
            val action =
                TicketCreateFragmentDirections.actionFragmentTicketCreateToFragmentTicketFinish(
                    photoUri
                )
            findNavController().navigate(action)
        }
    }

    private fun observeDescription() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.pbProgress.isVisible = true
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.description.collectLatest { data ->
                    data?.let {
                        binding.pbProgress.isVisible = false
                        binding.clProgress.visibility = View.GONE
                        Timber.tag("description").d(data.toString())
                        binding.apply {
                            etTitle.setText(data.title)
                            etDetail.setText(data.detail)
                            etSchedule.setText(data.schedule)
                            etSeat.setText(data.seat)
                        }
                    }
                }
            }
        }
    }

    private fun describeImage(uri: Uri) {
//        ImageProcessingUtil(requireContext()).uriToBase64(uri)?.let { base64String ->
//            viewModel.getDescription(base64String)
//        }
        Glide.with(binding.root.context).load(uri.toString()).centerCrop().into(binding.ivPoster)
    }

    private fun initGalleryPhoto() { //프로필 이미지
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.data?.let { uri ->
                        photoUri = uri
                        describeImage(uri)
                    }
                } else {
                    findNavController().popBackStack()
                }
            }
    }

    private fun initCamera() {
        takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    if (::photoUri.isInitialized) {
                        describeImage(photoUri)
                    }
                }
            }
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

}