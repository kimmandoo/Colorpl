package com.domain.usecaseimpl.review

import com.data.repository.ReviewRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.ReviewDetail
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetReviewDetailUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {

    suspend fun getReviewDetail(reviewId: Int): Flow<DomainResult<ReviewDetail>> {
        return flow {
            reviewRepository.getReviewDetail(reviewId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.Success(result.data.toEntity()))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.Error(result.exception))
                    }
                }
            }
        }
    }

}