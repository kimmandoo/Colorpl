package com.colorpl.di

import com.domain.usecaseimpl.token.AccessTokenUseCase
import com.domain.util.onFailure
import com.domain.util.onSuccess
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    private val accessTokenUseCase: AccessTokenUseCase
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestBuilder = originRequest.newBuilder()
        val accessToken: String? = runBlocking {
            var token: String? = ""
            accessTokenUseCase.getAccessToken().collectLatest { response ->
                response.onSuccess { data ->
                    token = data
                }
                response.onFailure {
                    token = null
                }
            }
            token
        }
        Timber.d("액세스 토큰 확인용 $accessToken")
        val request = requestBuilder.addHeader("Authorization", "Bearer $accessToken").build()
        return chain.proceed(request)
    }
}