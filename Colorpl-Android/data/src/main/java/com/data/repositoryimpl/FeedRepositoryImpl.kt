package com.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.data.datasource.FeedDataSource
import com.data.model.paging.Feed
import com.data.pagingsource.FeedPagingSource
import com.data.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val feedDataSource: FeedDataSource
) : FeedRepository {
    override fun getPagedFeed(): Flow<PagingData<Feed>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { FeedPagingSource(feedDataSource) },
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 5
    }
}