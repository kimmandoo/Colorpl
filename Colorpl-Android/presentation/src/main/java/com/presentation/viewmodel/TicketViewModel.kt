package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.usecase.TmapRouteUseCase
import com.naver.maps.geometry.LatLng
import com.presentation.util.Mode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val tmapRouteUseCase: TmapRouteUseCase,
) : ViewModel() {
    private val _routeData = MutableSharedFlow<List<LatLng>>()
    val routeData: SharedFlow<List<LatLng>> = _routeData.asSharedFlow()

    fun getRoute(myLocation: LatLng, destination: LatLng) {
        viewModelScope.launch {
            val routeData = mutableListOf<LatLng>()
            runCatching {
                tmapRouteUseCase(
                    startX = myLocation.longitude.toString(),
                    startY = myLocation.latitude.toString(),
                    endX = destination.longitude.toString(),
                    endY = destination.latitude.toString(),
                )
            }.onSuccess { response ->
                response.collectLatest { routes ->
                    for (route in routes.legs) {
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
                    _routeData.emit(routeData)
                }
            }.onFailure { e: Throwable ->
                Timber.tag("error").e(e)
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