package com.data.datasourceimpl

import com.data.api.FeedApi
import com.data.datasource.remote.ReviewDataSource
import com.data.model.request.RequestReviewEdit
import com.data.model.response.ResponseReviewCreate
import com.data.model.response.ResponseReviewEdit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReviewDataSourceImpl @Inject constructor(private val feedApi: FeedApi) : ReviewDataSource {
    override suspend fun createReview(
        memberId: Int,
        ticketId: Int,
        review: MultipartBody.Part?,
        request: RequestBody
    ): ResponseReviewCreate {
        return feedApi.createUserFeedData(memberId, ticketId, request = request, file = review)
    }

    override suspend fun deleteReview(reviewId: Int): ResponseReviewEdit {
        return feedApi.deleteFeedData(reviewId)
    }

    override suspend fun editReview(
        memberId: Int,
        reviewId: Int,
        requestReviewEdit: RequestReviewEdit
    ): ResponseReviewEdit {
        return feedApi.editUserFeedData(memberId, reviewId, requestReviewEdit)
    }
}