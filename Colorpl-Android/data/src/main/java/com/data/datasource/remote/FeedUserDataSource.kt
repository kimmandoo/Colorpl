package com.data.datasource.remote

import com.data.model.paging.Feed
import com.data.model.paging.ResponsePagedFeed

interface FeedUserDataSource {
    suspend fun getFeed(memberId: Int, page: Int, size: Int): ResponsePagedFeed
}