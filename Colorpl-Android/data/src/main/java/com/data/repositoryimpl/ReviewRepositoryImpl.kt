package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.ReviewDataSource
import com.data.model.request.RequestReviewCreate
import com.data.model.response.ResponseReviewCreate
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
                memberId = request.memberId,
                ticketId = request.ticketId,
                review = filePart,
                request = requestPart
            )
        })
    }
}