package com.domain.usecaseimpl.feed

import androidx.paging.PagingData
import androidx.paging.map
import com.data.repository.FeedRepository
import com.domain.mapper.toEntity
import com.domain.model.Feed
import com.domain.usecase.FeedUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FeedUserUseCaseImpl @Inject constructor(private val feedRepository: FeedRepository) :
    FeedUserUseCase {
    override fun invoke(memberId: Int): Flow<PagingData<Feed>> {
        return feedRepository.getPagedUserFeed(memberId)
            .map { pagingData ->
                pagingData.map { feed ->
                    feed.toEntity()
                }
            }
    }
}