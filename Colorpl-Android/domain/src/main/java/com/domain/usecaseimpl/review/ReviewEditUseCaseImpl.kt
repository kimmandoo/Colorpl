package com.domain.usecaseimpl.review

import com.data.repository.ReviewRepository
import com.data.util.ApiResult
import com.domain.mapper.toEditEntity
import com.domain.model.Review
import com.domain.usecase.ReviewEditUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ReviewEditUseCaseImpl @Inject constructor(private val reviewRepository: ReviewRepository) :
    ReviewEditUseCase {

    override suspend fun invoke(
        reviewId: Int,
        requestReviewEdit: Review
    ): Flow<DomainResult<Int>> = flow {
        reviewRepository.editReview(
            reviewId,
            requestReviewEdit.toEditEntity()
        ).collect { result ->
            when (result) {
                is ApiResult.Error -> {
                    Timber.tag("review").d("usecase edited error ${result.exception}")
                    result.exception.printStackTrace()
                    emit(DomainResult.error(result.exception))
                }

                is ApiResult.Success -> {
                    Timber.tag("review").d("usecase edited ${result.data.reviewId}")
                    emit(DomainResult.success(result.data.reviewId))
                }
            }

        }
    }
}