package com.data.api

import com.data.util.NETWORK_ERROR
import com.data.util.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): ApiResult<T> {
    return withContext(dispatcher) {
        try {
            ApiResult.success(apiCall())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ApiResult.error(Exception(NETWORK_ERROR))
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = throwable.response()?.errorBody()?.string()
                    Timber.d("에러 바디 데이터 확인 ${throwable.message()}")
                    ApiResult.error(Exception("HTTP 에러 $code: $errorResponse"))
                }

                else -> {
                    ApiResult.error(Exception("알 수 없는 에러: ${throwable.message}"))
                }
            }
        }
    }
}