package com.data.repository

import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface OpenAiRepository {
    suspend fun getChatCompletion(base64String: String): Flow<ApiResult<ResponseVision>>
}