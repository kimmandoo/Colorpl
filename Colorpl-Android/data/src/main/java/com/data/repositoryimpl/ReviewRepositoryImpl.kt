package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.ReviewDataSource
import com.data.model.request.RequestReviewCreate
import com.data.model.request.RequestReviewEdit
import com.data.model.response.ResponseReviewCreate
import com.data.model.response.ResponseReviewDetail
import com.data.model.response.ResponseReviewEdit
import com.data.repository.ReviewRepository
import com.data.util.ApiResult
import com.data.util.FormDataConverterUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(private val reviewDataSource: ReviewDataSource) :
    ReviewRepository {
    override suspend fun createReview(
        ticketId: Int,
        request: RequestReviewCreate,
        file: File?
    ): Flow<ApiResult<ResponseReviewCreate>> = flow {
        emit(safeApiCall {
            val requestPart = FormDataConverterUtil.getJsonRequestBody(request)
            val filePart: MultipartBody.Part? =
                FormDataConverterUtil.getNullableMultiPartBody("file", file)
            Timber.d("review: ${filePart}\n request:$requestPart")
            reviewDataSource.createReview(
                ticketId = request.ticketId,
                review = filePart,
                request = requestPart
            )
        })
    }

    override suspend fun getReviewDetail(reviewId: Int): Flow<ApiResult<ResponseReviewDetail>> =
        flow {
            emit(safeApiCall {
                reviewDataSource.getReviewDetail(reviewId)
            })
        }
    override suspend fun deleteReview(reviewId: Int): Flow<ApiResult<ResponseReviewEdit>> = flow {
        emit(safeApiCall {
            reviewDataSource.deleteReview(reviewId)
        })
    }

    override suspend fun editReview(
        reviewId: Int,
        requestReviewEdit: RequestReviewEdit
    ): Flow<ApiResult<ResponseReviewEdit>> = flow {
        emit(safeApiCall {
            reviewDataSource.editReview(reviewId, requestReviewEdit)
        })
    }

}