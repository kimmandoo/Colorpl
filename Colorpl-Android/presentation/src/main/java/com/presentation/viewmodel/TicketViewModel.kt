package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.ReviewDetail
import com.domain.model.Route
import com.domain.model.TicketRequest
import com.domain.model.TicketResponse
import com.domain.usecase.TicketUseCase
import com.domain.usecase.TmapRouteUseCase
import com.domain.usecaseimpl.review.GetReviewDetailUseCase
import com.domain.util.DomainResult
import com.domain.util.onFailure
import com.domain.util.onSuccess
import com.naver.maps.geometry.LatLng
import com.presentation.util.Mode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val tmapRouteUseCase: TmapRouteUseCase,
    private val getDetailReviewDetailUseCase: GetReviewDetailUseCase,
    private val ticketUseCase: TicketUseCase
) : ViewModel() {

    private val _routeData = MutableSharedFlow<Pair<Route, List<LatLng>>>()
    val routeData: SharedFlow<Pair<Route, List<LatLng>>> = _routeData.asSharedFlow()
    private val _latLng = MutableStateFlow(LatLng(0.0, 0.0))
    val latLng: StateFlow<LatLng> get() = _latLng
    private val _isRouteNull = MutableSharedFlow<Boolean>()
    val isRouteNull: SharedFlow<Boolean> get() = _isRouteNull
    private val _reviewDetail = MutableStateFlow(ReviewDetail())
    val reviewDetail: StateFlow<ReviewDetail> get() = _reviewDetail
    private val _ticketData = MutableSharedFlow<TicketResponse>()
    val ticketData: SharedFlow<TicketResponse> get() = _ticketData

    fun setLatLng(value: LatLng) {
        _latLng.value = value
    }

    fun getSingleTicket(id: Int) {
        viewModelScope.launch {
            ticketUseCase.getSingleTicket(id).collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        Timber.d("티켓 상세 데이터 에러 ${it.exception}")
                    }

                    is DomainResult.Success -> {
                        _ticketData.emit(it.data)
                        Timber.d("티켓 상세 데이터 ${it.data}")
                    }
                }
            }
        }
    }

    fun putSingleTicket(file: File, ticket: TicketRequest) {
        viewModelScope.launch {
            ticketUseCase.putTicket(ticketData.last().id, file, ticket).collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        Timber.d("티켓 수정 데이터 에러 ${it.exception}")
                    }

                    is DomainResult.Success -> {
                        Timber.d("티켓 수정 데이터 성공 ${it.data}")
                    }
                }
            }
        }
    }

    fun deleteTicket(id: Int) {
        viewModelScope.launch {
            ticketUseCase.deleteTicket(id).collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        Timber.d("티켓 삭제 데이터 에러 ${it.exception}")
                    }

                    is DomainResult.Success -> {
                        Timber.d("티켓 삭제 데이터 성공 ${it.data}")
                    }
                }
            }
        }
    }

    fun getDetailReviewDetail(reviewId: Int) {
        viewModelScope.launch {
            getDetailReviewDetailUseCase.getReviewDetail(reviewId).collectLatest {
                when (it) {
                    is DomainResult.Success -> {
                        _reviewDetail.value = it.data
                    }

                    is DomainResult.Error -> {
                        Timber.d("리뷰 상세 데이터 에러 ${it.exception}")
                    }
                }
            }
        }
    }


    fun getRoute(destination: LatLng) {
        viewModelScope.launch {
            val routeData = mutableListOf<LatLng>()
            tmapRouteUseCase(
                startX = latLng.value.longitude.toString(),
                startY = latLng.value.latitude.toString(),
                endX = destination.longitude.toString(),
                endY = destination.latitude.toString(),
            ).collectLatest { response ->
                response.onSuccess { data ->
                    if (data == null) {
                        _isRouteNull.emit(true)
                    }
                    data?.let {
                        for (route in data.legs) {
                            when (route.mode) {
                                Mode.WALK.mode -> {
                                    route.steps?.let { steps ->
                                        for (lineString in steps) {
                                            routeData.addAll(parseLatLng(lineString.linestring))
                                        }
                                    }
                                }

                                else -> {
                                    route.passShape?.let { lineString ->
                                        routeData.addAll(parseLatLng(lineString))
                                    }
                                }
                            }
                        }
                        val pairData = Pair(data, routeData)
                        _routeData.emit(pairData)
                    }
                }.onFailure { error ->
                    Timber.tag("error").e(error)
                }
            }
        }
    }

    private fun parseLatLng(lineString: String): List<LatLng> {
        return lineString.split(" ").map { coords ->
            val (longitude, latitude) = coords.split(",")
            LatLng(latitude.toDouble(), longitude.toDouble())
        }
    }


}