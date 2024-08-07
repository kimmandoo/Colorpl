package com.data.repository

import com.data.model.request.RequestSignToken
import com.data.model.response.ResponseSignToken
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    suspend fun setSignToken(requestSignToken: RequestSignToken)

    suspend fun getSignToken(): Flow<ApiResult<ResponseSignToken>>

    suspend fun getAccessToken(): Flow<ApiResult<String>>

    suspend fun setAccessToken(accessToken: String)
}