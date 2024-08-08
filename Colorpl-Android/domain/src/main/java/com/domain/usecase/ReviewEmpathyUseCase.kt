package com.domain.usecase

import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface ReviewEmpathyUseCase {
    suspend fun addEmpathy(reviewId: Int): Flow<DomainResult<Int>>
    suspend fun removeEmpathy(reviewId: Int): Flow<DomainResult<Int>>
}