package com.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ImageProcessingUtil(private val context: Context) {

    fun processImageForVisionAPI(imageUri: Uri, maxWidth: Int = 1024, maxHeight: Int = 1024, quality: Int = 85): ByteArray {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        var bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        bitmap = resizeBitmap(bitmap, maxWidth, maxHeight)
        bitmap = convertToGrayscale(bitmap)
        val compressedImage = compressBitmap(bitmap, quality)

        return compressedImage
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val scaleWidth = maxWidth.toFloat() / width
        val scaleHeight = maxHeight.toFloat() / height
        val scaleFactor = scaleWidth.coerceAtMost(scaleHeight)

        val matrix = Matrix()
        matrix.postScale(scaleFactor, scaleFactor)

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    private fun compressBitmap(bitmap: Bitmap, quality: Int): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val compressFormat = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> Bitmap.CompressFormat.WEBP_LOSSY
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 -> Bitmap.CompressFormat.WEBP
            else -> Bitmap.CompressFormat.WEBP
        }
        bitmap.compress(compressFormat, quality, outputStream)
        return outputStream.toByteArray()
    }

    private fun convertToGrayscale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap.getPixel(x, y)
                val gray = (Color.red(pixel) * 0.299 + Color.green(pixel) * 0.587 + Color.blue(pixel) * 0.114).toInt()
                grayscaleBitmap.setPixel(x, y, Color.rgb(gray, gray, gray))
            }
        }

        return grayscaleBitmap
    }

    fun uriToBase64(uri: Uri): String? {
        return try {
            val byteArray = processImageForVisionAPI(uri)
            // Base64로 인코딩
            val base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            return "data:image/webp;base64,$base64String"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun cropBitmap(bitmap: Bitmap, left: Int, top: Int, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(bitmap, left, top, width, height)
    }

    fun uriToFile(uri: Uri): File? {
        val fileName = getFileName(uri)
        val cacheDir = context.externalCacheDir
        val file = File(cacheDir, fileName)

        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri): String {
        var fileName = ""
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        if (fileName.isEmpty()) {
            fileName = uri.path?.split('/')?.last() ?: "unknown"
        }
        return fileName
    }
}