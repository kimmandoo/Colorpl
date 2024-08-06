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
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ReviewCreateUseCaseImpl @Inject constructor(private val reviewRepository: ReviewRepository) :
    ReviewCreateUseCase {
    override fun invoke(image: File?, review: Review): Flow<DomainResult<Int>> = flow {
        reviewRepository.createReview(review.ticketId, review.toEntity(), image)
            .collect {  result ->
                when (result) {
                    is ApiResult.Error -> {
                        Timber.tag("review").d("usecase create ${result.exception}")
                        result.exception.printStackTrace()
                        emit(DomainResult.error(result.exception))
                    }

                    is ApiResult.Success -> {
                        Timber.tag("review").d("usecase create ${result.data.reviewId}")
                        emit(DomainResult.success(result.data.reviewId))
                    }
                }
            }
    }
}