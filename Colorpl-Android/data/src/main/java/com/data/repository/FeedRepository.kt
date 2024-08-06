package com.data.repository

import androidx.paging.PagingData
import com.data.model.paging.Feed
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getPagedFeed(): Flow<PagingData<Feed>>

    fun getPagedUserFeed(memberId: Int): Flow<PagingData<Feed>>
}