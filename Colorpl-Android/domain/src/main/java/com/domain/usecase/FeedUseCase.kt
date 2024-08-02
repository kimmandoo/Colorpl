package com.domain.usecase

import androidx.paging.PagingData
import com.domain.model.Feed
import kotlinx.coroutines.flow.Flow

interface FeedUseCase {
    operator fun invoke(): Flow<PagingData<Feed>>
}