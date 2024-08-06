package com.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.data.datasource.CommentDataSource
import com.data.model.paging.Comment
import com.data.pagingsource.CommentPagingSource
import com.data.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentDataSource: CommentDataSource
) : CommentRepository {

    override fun getPagedComment(reviewId: Int): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { CommentPagingSource(reviewId, commentDataSource) },
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 5
    }
}