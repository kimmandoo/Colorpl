package com.domain.usecase

import com.domain.model.Review
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ReviewDeleteUseCase {
    operator fun invoke(
        reviewId: Int
    ): Flow<DomainResult<Int>>
}