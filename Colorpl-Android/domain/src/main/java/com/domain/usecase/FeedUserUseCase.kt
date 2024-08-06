package com.domain.usecase

import androidx.paging.PagingData
import com.domain.model.Feed
import kotlinx.coroutines.flow.Flow

interface FeedUserUseCase {
    operator fun invoke(memberId: Int): Flow<PagingData<Feed>>
}