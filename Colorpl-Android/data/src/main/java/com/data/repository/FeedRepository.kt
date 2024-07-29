package com.data.repository

import androidx.paging.PagingData
import com.data.model.paging.Feed
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getPagedFeed(): Flow<PagingData<Feed>>
}