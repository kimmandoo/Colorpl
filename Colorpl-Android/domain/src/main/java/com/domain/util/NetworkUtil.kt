package com.domain.util

suspend fun <T> DomainResult<T>.onSuccess(block: suspend (T) -> Unit): DomainResult<T> {
    if (this is DomainResult.Success) {
        block(data)
    }
    return this
}

suspend fun <T> DomainResult<T>.onFailure(block: suspend (Exception) -> Unit): DomainResult<T> {
    if (this is DomainResult.Error) {
        block(exception)
    }
    return this
}