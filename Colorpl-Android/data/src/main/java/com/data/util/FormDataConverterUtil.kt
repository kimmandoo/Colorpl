package com.data.util

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object FormDataConverterUtil {
    private const val MIME_TYPE_IMAGE = "image/*"
    private const val MIME_TYPE_TEXT = "text/plain"
    private const val MIME_TYPE_JSON = "application/json"

    fun getRequestBody(value: String): RequestBody {
        return value.toRequestBody(MIME_TYPE_TEXT.toMediaType())
    }

    fun getJsonRequestBody(value: Any?): RequestBody {
        return Gson().toJson(value).toRequestBody(MIME_TYPE_JSON.toMediaType())
    }

    fun getMultiPartBody(key: String, value: Any): MultipartBody.Part {
        return MultipartBody.Part.createFormData(key, value.toString())
    }

    fun getMultiPartBody(key: String, file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = key,
            filename = file.name,
            body = file.asRequestBody(MIME_TYPE_IMAGE.toMediaType()),
        )
    }

    fun getNullableMultiPartBody(key: String, file: File?): MultipartBody.Part? {
        return file?.asRequestBody(MIME_TYPE_IMAGE.toMediaType())?.let {
            MultipartBody.Part.createFormData(
                name = key,
                filename = file.name,
                body = it,
            )
        }
    }
}