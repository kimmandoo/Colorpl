package com.domain.usecaseimpl.review

import com.data.repository.ReviewRepository
import com.data.util.ApiResult
import com.domain.usecase.ReviewDeleteUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ReviewDeleteUseCaseImpl @Inject constructor(private val reviewRepository: ReviewRepository) :
    ReviewDeleteUseCase {

    override fun invoke(reviewId: Int): Flow<DomainResult<Int>> = flow {
        reviewRepository.deleteReview(reviewId).collect { result ->
            when (result) {
                is ApiResult.Error -> {
                    Timber.tag("review").d("usecase deleted error ${result.exception}")
                    result.exception.printStackTrace()
                    emit(DomainResult.error(result.exception))
                }

                is ApiResult.Success -> {
                    Timber.tag("review").d("usecase deleted ${result.data.reviewId}")
                    emit(DomainResult.success(result.data.reviewId))
                }
            }

        }
    }
}