package com.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.data.api.safeApiCall
import com.data.datasource.FeedDataSource
import com.data.model.paging.Feed
import com.data.util.ApiResult
import timber.log.Timber

class FeedPagingSource(private val feedDataSource: FeedDataSource) :
    PagingSource<Int, Feed>() {
    override fun getRefreshKey(state: PagingState<Int, Feed>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        val nextPage = params.key ?: 0

        return when (val result =
            safeApiCall { feedDataSource.getFeed(nextPage, params.loadSize) }) {
            is ApiResult.Success -> {
                val response = result.data.items
                Timber.tag("pager").d("${response}")

                LoadResult.Page(
                    data = response,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (response.isEmpty() || result.data.totalPage == nextPage) null else nextPage + 1
                )
            }

            is ApiResult.Error -> {
                Timber.e(result.exception, "Error loading feed data for page $nextPage")
                LoadResult.Error(result.exception)
            }
        }
    }
}