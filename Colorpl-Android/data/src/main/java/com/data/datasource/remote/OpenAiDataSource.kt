package com.data.datasource.remote

import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision


interface OpenAiDataSource {

    suspend fun getChatCompletion(request: RequestVision): ResponseVision
}