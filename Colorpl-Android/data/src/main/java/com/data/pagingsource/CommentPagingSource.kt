package com.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.data.api.safeApiCall
import com.data.datasource.CommentDataSource
import com.data.model.paging.Comment
import com.data.util.ApiResult
import timber.log.Timber

class CommentPagingSource(
    private val feedId: Int,
    private val commentDataSource: CommentDataSource
) :
    PagingSource<Int, Comment>() {
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val nextPage = params.key ?: 0

        return when (val result =
            safeApiCall { commentDataSource.getComment(feedId, nextPage, params.loadSize) }) {
            is ApiResult.Success -> {
                val response = result.data
                Timber.tag("pager").d("${response.items.firstOrNull()?.id}")

                LoadResult.Page(
                    data = response.items,
                    prevKey = if (nextPage == 0) null else nextPage - 1,
                    nextKey = if (response.items.isEmpty() || nextPage >= response.totalPage) null else nextPage + 1
                )
            }

            is ApiResult.Error -> {
                Timber.e(result.exception, "Error loading feed data for page $nextPage")
                LoadResult.Error(result.exception)
            }
        }
    }
}