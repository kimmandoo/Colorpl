package com.domain.usecase

import com.domain.model.Description
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface OpenAiUseCase {
    suspend operator fun invoke(
        base64String: String
    ): Flow<DomainResult<Description>>
}