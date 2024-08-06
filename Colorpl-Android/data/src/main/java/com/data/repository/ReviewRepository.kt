package com.data.repository

import com.data.model.request.RequestReviewCreate
import com.data.model.response.ResponseReviewCreate
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ReviewRepository {
    suspend fun createReview(
        ticketId: Int,
        request: RequestReviewCreate,
        file: File?
    ): Flow<ApiResult<ResponseReviewCreate>>
}