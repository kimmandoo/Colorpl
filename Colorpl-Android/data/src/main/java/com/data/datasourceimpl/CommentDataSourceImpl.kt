package com.data.datasourceimpl

import com.data.api.FeedApi
import com.data.datasource.CommentDataSource
import com.data.model.paging.ResponsePagedComment
import javax.inject.Inject

class CommentDataSourceImpl @Inject constructor(
    private val feedApi: FeedApi
): CommentDataSource{
    override suspend fun getComment(reviewId: Int, page: Int, size: Int): ResponsePagedComment {
        return feedApi.getCommentData(reviewId, page, size)
    }
}