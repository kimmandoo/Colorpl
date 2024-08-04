package com.data.repository

import androidx.paging.PagingData
import com.data.model.paging.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getPagedComment(reviewId: Int): Flow<PagingData<Comment>>
}