package com.data.util

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception) : ApiResult<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(exception: Exception) = Error(exception)
    }
}