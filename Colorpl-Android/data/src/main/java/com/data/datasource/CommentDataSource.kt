package com.data.datasource

import com.data.model.paging.ResponsePagedComment

interface CommentDataSource {
    suspend fun getComment(reviewId: Int, page: Int, size: Int): ResponsePagedComment
}