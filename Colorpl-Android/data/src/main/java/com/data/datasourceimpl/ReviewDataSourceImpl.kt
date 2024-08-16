package com.data.datasourceimpl

import com.data.api.FeedApi
import com.data.datasource.remote.ReviewDataSource
import com.data.model.request.RequestReviewEdit
import com.data.model.response.ResponseReviewCreate
import com.data.model.response.ResponseReviewDetail
import com.data.model.response.ResponseReviewEdit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReviewDataSourceImpl @Inject constructor(private val feedApi: FeedApi) : ReviewDataSource {
    override suspend fun createReview(
        review: MultipartBody.Part?,
        request: RequestBody
    ): ResponseReviewCreate {
        return feedApi.createUserFeedData(request = request, file = review)
    }


    override suspend fun getReviewDetail(reviewId: Int): ResponseReviewDetail {
        return feedApi.getDetailFeedData(reviewId)
    }

    override suspend fun addEmpathize(reviewId: Int): ResponseReviewEdit {
        return feedApi.createEmpathizeData(reviewId)
    }

    override suspend fun deleteEmpathize(reviewId: Int): ResponseReviewEdit {
        return feedApi.deleteEmpathizeData(reviewId)
    }

    override suspend fun deleteReview(reviewId: Int): ResponseReviewEdit {
        return feedApi.deleteFeedData(reviewId)
    }

    override suspend fun editReview(
        reviewId: Int,
        requestReviewEdit: RequestReviewEdit
    ): ResponseReviewEdit {
        return feedApi.editUserFeedData(reviewId, requestReviewEdit)
    }
}
