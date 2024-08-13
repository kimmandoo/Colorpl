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
    private val filters: Map<String, String?>

) : PagingSource<Int, Show>() {
    override fun getRefreshKey(state: PagingState<Int, Show>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        val cursorId = params.key
        filters + ("limit" to 20.toString())

        // cursorId가 null이면 초기 호출을 의미하므로 필터에서 해당 항목을 제거합니다.
        val updatedFilters = if (cursorId != null) {
            filters + ("cursorId" to cursorId.toString())
        } else {
            filters - "cursorId"
        }

        return when (val result =
            safeApiCall { reservationDataSource.getReservationListShows(updatedFilters) }) {
            is ApiResult.Success -> {
                val response = result.data.items
                Timber.tag("pagedShow").d("${response}")
                // 다음 페이지의 cursorId는 현재 페이지의 마지막 아이템 id로 설정합니다.
                val nextCursorId = if (response.isEmpty()) null else response.last().id

                LoadResult.Page(
                    data = response,
                    prevKey = null, // 이전 페이지에 대한 처리가 필요 없으므로 null로 설정
                    nextKey = nextCursorId // 다음 페이지의 cursorId를 설정
                )
            }
            is ApiResult.Error -> {
                Timber.tag("pagedShow").e("에러")
                LoadResult.Error(result.exception)
            }
        }
    }


}