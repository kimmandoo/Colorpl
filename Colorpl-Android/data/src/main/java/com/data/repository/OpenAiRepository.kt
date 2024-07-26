package com.data.repository

import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface OpenAiRepository {
    suspend fun getChatCompletion(requestVision: RequestVision): Flow<ApiResult<ResponseVision>>
}