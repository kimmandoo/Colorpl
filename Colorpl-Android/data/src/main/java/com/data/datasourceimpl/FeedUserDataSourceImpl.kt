package com.data.datasourceimpl

import com.data.api.FeedApi
import com.data.datasource.remote.FeedUserDataSource
import com.data.model.paging.ResponsePagedFeed
import javax.inject.Inject

class FeedUserDataSourceImpl @Inject constructor(
    private val feedApi: FeedApi
) : FeedUserDataSource {
    override suspend fun getFeed(memberId: Int, page: Int, size: Int): ResponsePagedFeed {
        return feedApi.getUserFeedData(memberId, page, size)
    }
}