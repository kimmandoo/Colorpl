package com.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.data.api.safeApiCall
import com.data.datasource.CommentDataSource
import com.data.model.paging.Comment
import com.data.model.request.RequestCreateComment
import com.data.model.response.ResponseCommentEdit
import com.data.pagingsource.CommentPagingSource
import com.data.repository.CommentRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override suspend fun createComment(
        reviewId: Int,
        requestCreateComment: RequestCreateComment
    ): Flow<ApiResult<ResponseCommentEdit>> = flow {
        emit(safeApiCall {
            commentDataSource.createComment(reviewId, requestCreateComment)
        })
    }

    override suspend fun editComment(
        commentId: Int,
        requestEditComment: RequestCreateComment
    ): Flow<ApiResult<ResponseCommentEdit>> = flow {
        emit(safeApiCall {
            commentDataSource.editComment(commentId, requestEditComment)
        })
    }

    override suspend fun deleteComment(commentId: Int): Flow<ApiResult<ResponseCommentEdit>> =
        flow {
            emit(safeApiCall {
                commentDataSource.deleteComment(commentId)
            })
        }

    companion object {
        private const val PAGE_SIZE = 5
    }
}