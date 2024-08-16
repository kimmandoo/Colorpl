package com.domain.usecaseimpl.review

import com.data.repository.ReviewRepository
import com.data.util.ApiResult
import com.domain.usecase.ReviewEmpathyUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReviewEmpathyUseCaseImpl @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ReviewEmpathyUseCase {
    override suspend fun addEmpathy(reviewId: Int): Flow<DomainResult<Int>> = flow {
        reviewRepository.addEmpathize(reviewId).collect {
            when (it) {
                is ApiResult.Success -> {
                    emit(DomainResult.Success(it.data.reviewId))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.Error(it.exception))
                }
            }
        }
    }

    override suspend fun removeEmpathy(reviewId: Int): Flow<DomainResult<Int>> = flow {
        reviewRepository.deleteEmpathize(reviewId).collect {
            when (it) {
                is ApiResult.Success -> {
                    emit(DomainResult.Success(it.data.reviewId))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.Error(it.exception))
                }
            }
        }
    }
}