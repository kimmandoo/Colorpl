package com.domain.usecase

import androidx.paging.PagingData
import com.data.model.paging.Comment
import kotlinx.coroutines.flow.Flow

interface CommentUseCase {
    operator fun invoke(feedId: Int): Flow<PagingData<com.domain.model.Comment>>
}