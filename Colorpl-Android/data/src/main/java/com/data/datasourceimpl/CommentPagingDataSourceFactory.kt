package com.data.datasourceimpl

import com.data.api.FeedApi
import javax.inject.Inject

class CommentPagingDataSourceFactory @Inject constructor(private val api: FeedApi) {
    fun create(feedId: Int): CommentPagingDataSourceImpl {
        return CommentPagingDataSourceImpl.create(api, feedId)
    }
}