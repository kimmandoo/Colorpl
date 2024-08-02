package com.presentation.util

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.ext.SdkExtensions
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun getPhotoGallery(launcher: ActivityResultLauncher<Intent>) { // 확장해서 쓸 수 있을듯
    val intentAction =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                Build.VERSION_CODES.R
            ) >= 2
        ) {
            MediaStore.ACTION_PICK_IMAGES
        } else {
            Intent.ACTION_PICK
        }
    launcher.launch(Intent(
        intentAction
    ).apply {
        type = "image/*"
    })
}

fun Fragment.setImageLauncher(action: (Uri) -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            it.data?.data?.let { uri ->
                action(uri)
            }
        } else {
            findNavController().popBackStack()
        }
    }
}

fun Fragment.setCameraLauncher(action: () -> Unit): ActivityResultLauncher<Uri> {
    return registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            action()
        }
    }
}