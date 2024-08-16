package com.domain.usecaseimpl.feed

import androidx.paging.PagingData
import androidx.paging.map
import com.data.repository.FeedRepository
import com.domain.mapper.toEntity
import com.domain.model.Feed
import com.domain.usecase.FeedUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class FeedUseCaseImpl @Inject constructor(private val feedRepository: FeedRepository) :
    FeedUseCase {

    override fun getPagedFeed(): Flow<PagingData<Feed>> {
        return feedRepository.getPagedFeed()
            .map { pagingData ->
                Timber.tag("pagerUseCase").d("$pagingData")
                pagingData.map { feed ->
                    feed.toEntity()
                }
            }
    }

    override fun getPagedMyFeed(): Flow<PagingData<Feed>> {
        return feedRepository.getPagedMyFeed()
            .map { pagingData ->
                Timber.tag("pagerUseCase").d("$pagingData")
                pagingData.map { feed ->
                    feed.toEntity()
                }
            }
    }
}