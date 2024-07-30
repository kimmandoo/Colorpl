package com.data.repository

import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    suspend fun getAccessToken() : Flow<ApiResult<String>>

    suspend fun setAccessToken(accessToken : String)
}