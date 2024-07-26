package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.OpenAiDataSource
import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision
import com.data.repository.OpenAiRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenAiRepositoryImpl @Inject constructor(
    private val openAiDataSource: OpenAiDataSource
) : OpenAiRepository {
    override suspend fun getChatCompletion(requestVision: RequestVision): Flow<ApiResult<ResponseVision>> =
        flow {
            emit(safeApiCall {
                openAiDataSource.getChatCompletion(
                    requestVision
                )
            })
        }
}