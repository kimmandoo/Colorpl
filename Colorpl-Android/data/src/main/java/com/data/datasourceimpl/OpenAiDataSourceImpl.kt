package com.data.datasourceimpl

import com.data.api.OpenAiApi
import com.data.datasource.OpenAiDataSource
import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision
import javax.inject.Inject

class OpenAiDataSourceImpl @Inject constructor(
    private val openAiApi: OpenAiApi
) : OpenAiDataSource {
    override suspend fun getChatCompletion(request: RequestVision): ResponseVision {
        return openAiApi.getChatCompletion(request)
    }
}