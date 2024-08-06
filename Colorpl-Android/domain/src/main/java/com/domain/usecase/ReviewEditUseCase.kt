package com.domain.usecase

import com.domain.model.Review
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface ReviewEditUseCase {
    suspend operator fun invoke(reviewId: Int, requestReviewEdit: Review): Flow<DomainResult<Int>>
}