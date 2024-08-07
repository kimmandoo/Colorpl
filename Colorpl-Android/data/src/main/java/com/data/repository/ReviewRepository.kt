package com.data.repository

import com.data.model.request.RequestReviewCreate
import com.data.model.request.RequestReviewEdit
import com.data.model.response.ResponseReviewCreate
import com.data.model.response.ResponseReviewDetail
import com.data.model.response.ResponseReviewEdit
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ReviewRepository {
    suspend fun createReview(
        ticketId: Int,
        request: RequestReviewCreate,
        file: File?
    ): Flow<ApiResult<ResponseReviewCreate>>


    suspend fun getReviewDetail(reviewId : Int) : Flow<ApiResult<ResponseReviewDetail>>

    suspend fun deleteReview(reviewId: Int): Flow<ApiResult<ResponseReviewEdit>>

    suspend fun editReview(reviewId: Int, requestReviewEdit: RequestReviewEdit): Flow<ApiResult<ResponseReviewEdit>>


}