package com.presentation.ticket

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketCreateBinding
import com.domain.model.Description
import com.presentation.base.BaseFragment
import com.presentation.component.dialog.LoadingDialog
import com.presentation.util.ImageProcessingUtil
import com.presentation.util.TicketType
import com.presentation.util.checkCameraPermission
import com.presentation.util.getPhotoGallery
import com.presentation.util.setCameraLauncher
import com.presentation.util.setImageLauncher
import com.presentation.viewmodel.TicketCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        observeDescription()
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
        binding.rgCategory.setOnCheckedChangeListener { radioGroup, itemId ->
            val selectedText = when (itemId) {
                R.id.rb_movie -> binding.rbMovie
                R.id.rb_show -> binding.rbShow
                R.id.rb_concert -> binding.rbConcert
                R.id.rb_play -> binding.rbPlay
                R.id.rb_musical -> binding.rbMusical
                R.id.rb_exhibition -> binding.rbExhibition
                R.id.rb_else -> binding.rbElse
                else -> null
            }?.text?.toString()

            selectedText?.let { viewModel.setCategory(it) }
        }
    }

    private fun observeDescription() {
        viewLifecycleOwner.lifecycleScope.launch {
            val loading = LoadingDialog(requireContext())
            loading.show()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.description.collectLatest { data ->
                    data?.let {
                        loading.dismiss()
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
        viewModel.category.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { state ->
            Timber.d("$state")
            binding.tvConfirm.isSelected = state != ""
        }.launchIn(viewLifecycleOwner.lifecycleScope)
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
        takePicture = setCameraLauncher {
            if (::photoUri.isInitialized) {
                describeImage(photoUri)
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