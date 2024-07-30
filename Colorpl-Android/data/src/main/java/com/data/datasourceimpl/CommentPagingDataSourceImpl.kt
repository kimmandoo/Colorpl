package com.data.datasourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.data.api.FeedApi
import com.data.api.safeApiCall
import com.data.model.paging.Comment
import com.data.util.ApiResult
import timber.log.Timber
import javax.inject.Inject

class CommentPagingDataSourceImpl private constructor(
    private val api: FeedApi,
    private val feedId: Int
) :
    PagingSource<Int, Comment>() {
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val nextPage = params.key ?: 1

        return when (val result =
            safeApiCall { api.getCommentData(feedId, nextPage, params.loadSize) }) {
            is ApiResult.Success -> {
                val response = result.data
                Timber.tag("pager").d("${response.items.firstOrNull()?.commentId}")

                LoadResult.Page(
                    data = response.items,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (response.items.isEmpty() || nextPage >= response.totalPages) null else nextPage + 1
                )
            }

            is ApiResult.Error -> {
                Timber.e(result.exception, "Error loading feed data for page $nextPage")
                LoadResult.Error(result.exception)
            }
        }
    }

    companion object {
        fun create(api: FeedApi, feedId: Int): CommentPagingDataSourceImpl {
            return CommentPagingDataSourceImpl(api, feedId)
        }
    }
}