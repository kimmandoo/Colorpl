package com.domain.usecase

import androidx.paging.PagingData
import com.data.model.request.RequestCreateComment
import com.domain.model.Comment
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface CommentUseCase {
    fun getComment(reviewId: Int): Flow<PagingData<com.domain.model.Comment>>
    suspend fun editComment(
        comment: Comment
    ): Flow<DomainResult<Int>>

    suspend fun deleteComment(commentId: Int): Flow<DomainResult<Int>>
    suspend fun createComment(
        comment: Comment
    ): Flow<DomainResult<Int>>
}