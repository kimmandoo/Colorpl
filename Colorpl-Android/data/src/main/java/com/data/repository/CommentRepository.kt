package com.data.repository

import androidx.paging.PagingData
import com.data.model.paging.Comment
import com.data.model.request.RequestCreateComment
import com.data.model.response.ResponseCommentEdit
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getPagedComment(reviewId: Int): Flow<PagingData<Comment>>
    suspend fun createComment(
        reviewId: Int,
        requestCreateComment: RequestCreateComment
    ): Flow<ApiResult<ResponseCommentEdit>>

    suspend fun editComment(
        commentId: Int,
        requestEditComment: RequestCreateComment
    ): Flow<ApiResult<ResponseCommentEdit>>

    suspend fun deleteComment(commentId: Int): Flow<ApiResult<ResponseCommentEdit>>
}