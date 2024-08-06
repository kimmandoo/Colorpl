package com.data.datasource.remote

import com.data.model.response.ResponseReviewCreate
import com.data.model.response.ResponseReviewDetail
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ReviewDataSource {
    suspend fun createReview(
        memberId: Int,
        ticketId: Int,
        review: MultipartBody.Part?,
        request: RequestBody
    ): ResponseReviewCreate


    suspend fun getReviewDetail(
        reviewId: Int
    ) : ResponseReviewDetail
}