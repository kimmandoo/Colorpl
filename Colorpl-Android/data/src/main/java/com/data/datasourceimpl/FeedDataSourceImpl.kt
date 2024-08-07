package com.data.datasourceimpl

import com.data.api.FeedApi
import com.data.datasource.FeedDataSource
import com.data.model.paging.Feed
import com.data.model.paging.ResponsePagedFeed
import javax.inject.Inject

class FeedDataSourceImpl @Inject constructor(
    private val feedApi: FeedApi
) : FeedDataSource {
    override suspend fun getFeed(page: Int, size: Int): ResponsePagedFeed {
        return feedApi.getAllFeedData(page, size)
    }
}