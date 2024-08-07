package com.data.datasource

import com.data.model.paging.ResponsePagedComment
import com.data.model.request.RequestCreateComment
import com.data.model.response.ResponseCommentEdit

interface CommentDataSource {
    suspend fun createComment(
        reviewId: Int,
        requestCreateComment: RequestCreateComment
    ): ResponseCommentEdit

    suspend fun getComment(reviewId: Int, page: Int, size: Int): ResponsePagedComment
    suspend fun editComment(
        commentId: Int,
        requestEditComment: RequestCreateComment
    ): ResponseCommentEdit

    suspend fun deleteComment(commentId: Int): ResponseCommentEdit
}