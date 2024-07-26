package com.domain.util

sealed class RepoResult<out T> {
    data class Success<out T>(val data: T) : RepoResult<T>()
    data class Error(val exception: Exception) : RepoResult<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(exception: Exception) = Error(exception)
    }
}