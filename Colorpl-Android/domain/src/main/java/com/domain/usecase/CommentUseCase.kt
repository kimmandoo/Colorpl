package com.domain.usecase

import androidx.paging.PagingData
import com.domain.model.Comment
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface CommentUseCase {
    fun getComment(reviewId: Int): Flow<PagingData<Comment>>
    suspend fun editComment(
        reviewId: Int,
        commentId: Int,
        commentContent: String
    ): Flow<DomainResult<Int>>

    suspend fun deleteComment(commentId: Int): Flow<DomainResult<Int>>
    suspend fun createComment(
        reviewId: Int,
        commentContent: String
    ): Flow<DomainResult<Int>>
}