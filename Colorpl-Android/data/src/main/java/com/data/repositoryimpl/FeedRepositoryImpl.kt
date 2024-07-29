package com.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.data.datasourceimpl.FeedPagingDataSourceImpl
import com.data.model.paging.Feed
import com.data.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(private val feedPagingDataSource: FeedPagingDataSourceImpl) :
    FeedRepository {
    override fun getPagedFeed(): Flow<PagingData<Feed>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { feedPagingDataSource },
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}