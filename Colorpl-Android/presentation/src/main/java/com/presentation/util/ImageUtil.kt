package com.presentation.util

import android.content.Intent
import android.os.Build
import android.os.ext.SdkExtensions
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher

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