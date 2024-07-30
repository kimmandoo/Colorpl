package com.data.factory

import com.data.api.FeedApi
import com.data.datasourceimpl.CommentPagingDataSourceImpl
import javax.inject.Inject

class CommentPagingDataSourceFactory @Inject constructor(private val api: FeedApi) {
    fun create(feedId: Int): CommentPagingDataSourceImpl {
        return CommentPagingDataSourceImpl.create(api, feedId)
    }
}