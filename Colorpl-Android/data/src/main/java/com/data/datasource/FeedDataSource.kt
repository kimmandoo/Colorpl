package com.data.datasource

import com.data.model.paging.ResponsePagedFeed

interface FeedDataSource {
    suspend fun getFeed(page: Int, size: Int): ResponsePagedFeed

    suspend fun getMyFeedData(page: Int, size: Int): ResponsePagedFeed
}