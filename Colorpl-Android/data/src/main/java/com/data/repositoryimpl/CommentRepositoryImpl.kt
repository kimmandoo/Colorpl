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

    override fun getPagedComment(feedId: Int): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { CommentPagingSource(feedId, commentDataSource) },
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}