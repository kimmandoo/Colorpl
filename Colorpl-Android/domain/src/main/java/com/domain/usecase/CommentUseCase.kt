package com.domain.usecase

import androidx.paging.PagingData
import com.data.model.request.RequestCreateComment
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface CommentUseCase {
    fun getComment(feedId: Int): Flow<PagingData<com.domain.model.Comment>>
    suspend fun editComment(
        requestEditComment: RequestCreateComment
    ): Flow<DomainResult<Int>>

    suspend fun deleteComment(commentId: Int): Flow<DomainResult<Int>>
    suspend fun createComment(
        reviewCreateComment: RequestCreateComment
    ): Flow<DomainResult<Int>>
}