package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Route
import com.domain.usecase.TmapRouteUseCase
import com.naver.maps.geometry.LatLng
import com.presentation.util.Mode
import com.domain.util.onFailure
import com.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val tmapRouteUseCase: TmapRouteUseCase,
) : ViewModel() {

    private val _routeData = MutableSharedFlow<Pair<Route,List<LatLng>>>()
    val routeData: SharedFlow<Pair<Route,List<LatLng>>> = _routeData.asSharedFlow()

    private val _latLng = MutableStateFlow(LatLng(0.0,0.0))
    val latLng : StateFlow<LatLng> get() = _latLng

    fun setLatLng(value : LatLng){
        _latLng.value = value
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
                    for (route in data.legs) {
                        when (route.mode) {
                            Mode.WALK.mode -> {
                                for (lineString in route.steps!!) {
                                    routeData.addAll(parseLatLng(lineString.linestring))
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