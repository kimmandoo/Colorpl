package com.data.datasourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.data.api.FeedApi
import com.data.model.paging.Feed
import timber.log.Timber
import javax.inject.Inject

class FeedPagingDataSourceImpl @Inject constructor(private val api: FeedApi) :
    PagingSource<Int, Feed>() {
    override fun getRefreshKey(state: PagingState<Int, Feed>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        return try {
            val nextPage = params.key ?: 1
            val response = api.getFeedData(nextPage, params.loadSize)
            Timber.tag("pager").d("${response.items.first().feedId}")

            LoadResult.Page(
                data = response.items,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.items.isEmpty() || nextPage >= response.totalPages) null else nextPage + 1
            )
        } catch (e: Exception) {
            Timber.e(e, "Error loading feed data for page ${params.key}")
            LoadResult.Error(e)
        }
    }
}