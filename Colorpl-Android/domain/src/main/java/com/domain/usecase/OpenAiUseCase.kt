package com.domain.usecase

import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.Flow

interface OpenAiUseCase {
    suspend operator fun invoke(
        requestVision: RequestVision
    ): Flow<RepoResult<ResponseVision>> // 가공필요
}