package com.domain.usecaseimpl.openai

import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision
import com.data.repository.OpenAiRepository
import com.data.util.ApiResult
import com.domain.usecase.OpenAiUseCase
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenAiUseCaseImpl @Inject constructor(
    private val openAiRepository: OpenAiRepository
) : OpenAiUseCase {

    override suspend fun invoke(requestVision: RequestVision): Flow<RepoResult<ResponseVision>> =
        flow {
            openAiRepository.getChatCompletion(requestVision).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
//                    val route = result.data.toEntity()
//                    emit(RepoResult.success(route))
                    }

                    is ApiResult.Error -> {
                        emit(RepoResult.error(result.exception))
                    }
                }
            }
        }
}