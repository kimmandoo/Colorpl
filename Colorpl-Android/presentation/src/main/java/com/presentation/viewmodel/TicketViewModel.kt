package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.ReviewDetail
import com.domain.model.Route
import com.domain.model.TicketRequest
import com.domain.model.TicketResponse
import com.domain.usecase.GeocodingUseCase
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
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

data class RouteClassify(
    val mode: Int,
    val route: LatLng
)

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val tmapRouteUseCase: TmapRouteUseCase,
    private val getDetailReviewDetailUseCase: GetReviewDetailUseCase,
    private val ticketUseCase: TicketUseCase,
    private val geocodingUseCase: GeocodingUseCase
) : ViewModel() {

    private val _routeData = MutableSharedFlow<Pair<Route, MutableList<List<RouteClassify>>>>()
    val routeData: SharedFlow<Pair<Route, List<List<RouteClassify>>>> = _routeData.asSharedFlow()
    private val _latLng = MutableStateFlow(LatLng(0.0, 0.0))
    val latLng: StateFlow<LatLng> get() = _latLng
    private val _isRouteNull = MutableSharedFlow<Boolean>()
    val isRouteNull: SharedFlow<Boolean> get() = _isRouteNull
    private val _reviewDetail = MutableStateFlow(ReviewDetail())
    val reviewDetail: StateFlow<ReviewDetail> get() = _reviewDetail
    private val _ticketData = MutableSharedFlow<TicketResponse>()
    val ticketData: SharedFlow<TicketResponse> get() = _ticketData
    private val _ticketDeleteRespones = MutableSharedFlow<Boolean>()
    val ticketDeleteResponse: SharedFlow<Boolean> get() = _ticketDeleteRespones
    private val _ticketUpdateResponse = MutableSharedFlow<Int>()
    val ticketUpdateResponse: SharedFlow<Int> get() = _ticketUpdateResponse
    private val _geocodingLatLng = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val geocodingLatLng: StateFlow<LatLng> = _geocodingLatLng
    private val _juso = MutableStateFlow("")
    val juso: StateFlow<String> = _juso
    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category
    private val _schedule = MutableStateFlow("")
    val schedule: StateFlow<String> = _schedule

    fun setCategory(text: String) {
        _category.value = text
    }

    fun setJuso(text: String) {
        _juso.value = text
    }

    fun getAddress(address: String) {
        viewModelScope.launch {
            setJuso(address)
            geocodingUseCase(address).collectLatest { response ->
                when (response) {
                    is DomainResult.Error -> {
                        _geocodingLatLng.emit(LatLng(0.0, 0.0))
                    }

                    is DomainResult.Success -> {
                        Timber.tag("test").d("${LatLng(response.data.y, response.data.x)}")
                        _geocodingLatLng.emit(LatLng(response.data.y, response.data.x))
                    }
                }
            }
        }
    }

    fun setSchedule(text: String) {
        _schedule.value = text
    }

    fun cancelGetAddress() {
        viewModelScope.launch {
            _geocodingLatLng.value = LatLng(0.0, 0.0)
        }
    }

    fun setLatLng(value: LatLng) {
        _latLng.value = value
        _geocodingLatLng.value = value
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
                        setCategory(it.data.category)
                        setSchedule(it.data.dateTime)
                        Timber.d("티켓 상세 데이터 ${it.data}")
                    }
                }
            }
        }
    }

    fun editSingleTicket(id: Int, file: File?, ticket: TicketRequest) {
        Timber.tag("edit").d("editSingleTicket called with file: $file, ticket: $ticket")
        viewModelScope.launch {
            Timber.tag("edit").d("${id} \n${file} \n $ticket")
            ticketUseCase.putTicket(id, file, ticket).collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        _ticketUpdateResponse.emit(-1)
                        Timber.tag("edit").d("티켓 수정 데이터 에러 ${it.exception}")
                    }

                    is DomainResult.Success -> {
                        Timber.tag("edit ddd").d("티켓 수정 데이터 성공 ${it.data}")
                        _ticketUpdateResponse.emit(it.data)
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
                        _ticketDeleteRespones.emit(true)
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
            val routeSegments = mutableListOf<List<RouteClassify>>()
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
                        var currentSegment = mutableListOf<RouteClassify>()
                        var currentMode = -1

                        fun addCurrentSegment() {
                            if (currentSegment.isNotEmpty()) {
                                routeSegments.add(currentSegment)
                                currentSegment = mutableListOf()
                            }
                        }

                        for (route in data.legs) {
                            when (route.mode) {
                                Mode.WALK.mode -> {
                                    route.steps?.forEach { step ->
                                        val newPoints = parseLatLng(0, step.linestring)
                                        if (currentMode != 0) {
                                            addCurrentSegment()
                                            currentMode = 0
                                        }
                                        currentSegment.addAll(newPoints)
                                    }
                                }
                                Mode.BUS.mode, Mode.SUBWAY.mode -> {
                                    route.passShape?.let { lineString ->
                                        val newPoints = parseLatLng(1, lineString)
                                        if (currentMode != 1) {
                                            addCurrentSegment()
                                            currentMode = 1
                                        }
                                        currentSegment.addAll(newPoints)
                                    }
                                }
                                Mode.EXPRESS_BUS.mode ->{
                                    route.passShape?.let { lineString ->
                                        val newPoints = parseLatLng(2, lineString)
                                        if (currentMode != 2) {
                                            addCurrentSegment()
                                            currentMode = 2
                                        }
                                        currentSegment.addAll(newPoints)
                                    }
                                }
                                else -> {
                                    route.passShape?.let { lineString ->
                                        val newPoints = parseLatLng(1, lineString)
                                        if (currentMode != 1) {
                                            addCurrentSegment()
                                            currentMode = 1
                                        }
                                        currentSegment.addAll(newPoints)
                                    }
                                }
                            }
                        }
                        addCurrentSegment() // 마지막 세그먼트 추가

                        val pairData = Pair(data, routeSegments)
                        _routeData.emit(pairData)
                    }
                }.onFailure { error ->
                    Timber.tag("error").e(error)
                }
            }
        }
    }

    private fun parseLatLng(mode: Int, lineString: String): List<RouteClassify> {
        return lineString.split(" ").map { coords ->
            val (longitude, latitude) = coords.split(",")
            RouteClassify(
                mode = mode,
                route = LatLng(latitude.toDouble(), longitude.toDouble())
            )
        }
    }



}