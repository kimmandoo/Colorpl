package com.presentation.util

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.ext.SdkExtensions
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.naver.maps.map.overlay.OverlayImage
import okio.IOException
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL

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

fun combineImages(context: Context, markerResId: Int, innerImageBitmap: Bitmap): OverlayImage {
    // 첫 번째 이미지 비트맵 로드

    val markerBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, markerResId)



    val combinedBitmap = Bitmap.createScaledBitmap(markerBitmap, 75.dpToPx, 75.dpToPx, true)
    val canvas = Canvas(combinedBitmap)
    //내부 그림 크기 조정
    val scaledInnerBitmap = Bitmap.createScaledBitmap(innerImageBitmap, 45.dpToPx, 45.dpToPx, true)

    // 내부 그림 그리기 (말풍선 중앙에 위치)
    val left = (combinedBitmap.width-scaledInnerBitmap.width) / 2
    val top = (combinedBitmap.height-scaledInnerBitmap.height-10.dpToPx) /2


    canvas.drawBitmap(scaledInnerBitmap, left.toFloat(), top.toFloat(), null)

    return OverlayImage.fromBitmap(combinedBitmap)
}

fun convertBitmapFromURL(imageUrl: String): Bitmap? {
    try {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        val bitmap = BitmapFactory.decodeStream(input)
        return bitmap
    } catch (e: IOException) {
        Timber.e("지도 데이터 에러 $e")
    }
    return null
}