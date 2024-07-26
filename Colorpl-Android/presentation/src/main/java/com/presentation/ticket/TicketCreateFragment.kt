package com.presentation.ticket

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketCreateBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.presentation.base.BaseFragment
import com.presentation.util.TicketType
import com.presentation.util.getPhotoGallery
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class TicketCreateFragment :
    BaseFragment<FragmentTicketCreateBinding>(R.layout.fragment_ticket_create) {

    private val args: TicketCreateFragmentArgs by navArgs()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var photoUri: Uri

    override fun initView() {
        initGalleryPhoto()
        initCamera()
        initUi()
    }

    private fun initUi() {
        when (args.photoType) {
            TicketType.CAMERA -> {
                checkCameraPermission()
            }

            TicketType.GALLERY -> {
                getPhotoGallery(pickImageLauncher)
            }
        }
    }

    private fun describeImage(uri: Uri) {
        Timber.tag("image").d("$uri")
        Glide.with(binding.root.context).load(uri).centerCrop().into(binding.ivPoster)
    }

    private fun initGalleryPhoto() { //프로필 이미지
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.data?.let { uri ->
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


    private fun checkCameraPermission() {
        TedPermission.create().setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {
                openCamera()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

            }
        }).setDeniedMessage("앱을 사용하려면 권한이 필요합니다. [설정] > [권한]에서 권한을 허용해 주세요.")
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
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