package com.data.datasource

import com.data.model.paging.Feed
import com.data.model.paging.ResponsePagedFeed

interface FeedDataSource {
    suspend fun getFeed(page: Int, size: Int): List<Feed>
}