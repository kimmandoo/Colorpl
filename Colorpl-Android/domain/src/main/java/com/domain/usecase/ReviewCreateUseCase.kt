package com.domain.usecase

import com.domain.model.Review
import com.domain.model.Ticket
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ReviewCreateUseCase {
    operator fun invoke(
        image: File?,
        review: Review
    ): Flow<DomainResult<Int>>
}