package com.domain.usecase

import androidx.paging.PagingData
import com.domain.model.Feed
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.Flow

interface FeedUseCase {
    operator fun invoke(): Flow<PagingData<Feed>>
}