package com.data.datasource.remote

import com.data.model.request.RequestReviewEdit
import com.data.model.response.ResponseReviewCreate
import com.data.model.response.ResponseReviewEdit
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ReviewDataSource {
    suspend fun createReview(
        memberId: Int,
        ticketId: Int,
        review: MultipartBody.Part?,
        request: RequestBody
    ): ResponseReviewCreate

    suspend fun deleteReview(reviewId: Int): ResponseReviewEdit

    suspend fun editReview(memberId: Int, reviewId: Int, requestReviewEdit: RequestReviewEdit): ResponseReviewEdit
}