package com.domain.usecase

import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision
import com.domain.model.Description
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.Flow

interface OpenAiUseCase {
    suspend operator fun invoke(
        base64String: String
    ): Flow<RepoResult<Description>>
}