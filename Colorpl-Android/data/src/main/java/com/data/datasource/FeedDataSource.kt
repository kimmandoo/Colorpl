package com.data.datasource

import com.data.model.paging.ResponsePagedFeed

interface FeedDataSource {
    suspend fun getFeed(page: Int, items: Int): ResponsePagedFeed
}