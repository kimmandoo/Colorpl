package com.domain.usecase

import androidx.paging.PagingData
import com.domain.model.Feed
import kotlinx.coroutines.flow.Flow

interface FeedUseCase {
    fun getPagedFeed(): Flow<PagingData<Feed>>

    fun getPagedMyFeed(): Flow<PagingData<Feed>>
}