package com.presentation.util

import com.domain.util.RepoResult

suspend fun <T> RepoResult<T>.onSuccess(block: suspend (T) -> Unit): RepoResult<T> {
    if (this is RepoResult.Success) {
        block(data)
    }
    return this
}

suspend fun <T> RepoResult<T>.onFailure(block: suspend (Exception) -> Unit): RepoResult<T> {
    if (this is RepoResult.Error) {
        block(exception)
    }
    return this
}