package com.data.datasource

import com.data.model.paging.ResponsePagedComment

interface CommentDataSource {
    suspend fun getComment(feedId: Int, page: Int, items: Int): ResponsePagedComment
}