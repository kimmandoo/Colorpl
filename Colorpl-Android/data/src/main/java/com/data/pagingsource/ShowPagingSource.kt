package com.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.data.api.safeApiCall
import com.data.datasource.remote.ReservationDataSource
import com.data.model.paging.reservation.Show
import com.data.util.ApiResult
import timber.log.Timber

class ShowPagingSource(
    private val reservationDataSource: ReservationDataSource,
    private val filters: Map<String, String?>,
    private val limit: Int
) : PagingSource<Int, Show>() {

    override fun getRefreshKey(state: PagingState<Int, Show>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        val cursorId = params.key

        val updatedFilters = filters.toMutableMap().apply {
            put("limit", limit.toString())
            if (cursorId != null) {
                put("cursorId", cursorId.toString())
            } else {
                remove("cursorId")
            }
        }

        return when (val result = safeApiCall { reservationDataSource.getReservationListShows(updatedFilters) }) {
            is ApiResult.Success -> {
                val response = result.data
                Timber.tag("pagedShow").d("$response")
                val nextCursorId = response.lastOrNull()?.id

                LoadResult.Page(
                    data = response,
                    prevKey = null,
                    nextKey = nextCursorId // 다음 페이지 cursorId
                )
            }
            is ApiResult.Error -> {
                Timber.tag("pagedShow").e("에러: ${result.exception}")
                LoadResult.Error(result.exception)
            }
        }
    }
}