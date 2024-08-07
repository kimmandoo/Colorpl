package com.data.datasourceimpl

import com.data.api.FeedApi
import com.data.datasource.CommentDataSource
import com.data.model.paging.ResponsePagedComment
import com.data.model.request.RequestCreateComment
import com.data.model.response.ResponseCommentEdit
import javax.inject.Inject

class CommentDataSourceImpl @Inject constructor(
    private val feedApi: FeedApi
) : CommentDataSource {
    override suspend fun createComment(
        reviewId: Int,
        requestCreateComment: RequestCreateComment
    ): ResponseCommentEdit {
        return feedApi.createCommentData(reviewId, requestCreateComment)
    }

    override suspend fun getComment(reviewId: Int, page: Int, size: Int): ResponsePagedComment {
        return feedApi.getCommentData(reviewId, page, size)
    }

    override suspend fun editComment(
        commentId: Int,
        requestEditComment: RequestCreateComment
    ): ResponseCommentEdit {
        return feedApi.editCommentData(commentId, requestEditComment)
    }

    override suspend fun deleteComment(commentId: Int): ResponseCommentEdit {
        return feedApi.deleteCommentData(commentId)
    }
}