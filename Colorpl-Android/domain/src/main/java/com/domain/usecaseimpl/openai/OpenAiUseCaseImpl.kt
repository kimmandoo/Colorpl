package com.domain.usecaseimpl.openai

import com.data.model.request.RequestVision
import com.data.repository.OpenAiRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Description
import com.domain.usecase.OpenAiUseCase
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenAiUseCaseImpl @Inject constructor(
    private val openAiRepository: OpenAiRepository,
) : OpenAiUseCase {

    override suspend fun invoke(base64String: String): Flow<RepoResult<Description>> =
        flow {
            openAiRepository.getChatCompletion(base64String).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val description = result.data.toEntity()
                        emit(RepoResult.success(description))
                    }

                    is ApiResult.Error -> {
                        emit(RepoResult.error(result.exception))
                    }
                }
            }
        }
}