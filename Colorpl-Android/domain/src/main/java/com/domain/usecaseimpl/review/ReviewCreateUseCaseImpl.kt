package com.domain.usecaseimpl.review

import com.data.repository.ReviewRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Review
import com.domain.usecase.ReviewCreateUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class ReviewCreateUseCaseImpl @Inject constructor(private val reviewRepository: ReviewRepository) :
    ReviewCreateUseCase {
    override suspend fun invoke(image: File, review: Review): Flow<DomainResult<Int>> = flow {
        reviewRepository.createReview(review.ticketId, review.toEntity(), image)
            .collectLatest { result ->
                when (result) {
                    is ApiResult.Error -> {
                        emit(DomainResult.error(result.exception))
                    }

                    is ApiResult.Success -> {
                        emit(DomainResult.success(result.data.reviewId))
                    }
                }
            }
    }
}