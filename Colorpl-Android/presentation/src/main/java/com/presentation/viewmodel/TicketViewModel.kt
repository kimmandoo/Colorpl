package com.presentation.viewmodel

import TmapRoute
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.usecase.TmapRouteUseCase
import com.naver.maps.geometry.LatLng
import com.presentation.util.Mode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val tmapRouteUseCase: TmapRouteUseCase
) : ViewModel() {

    fun getRoute() {
        viewModelScope.launch {
            val routeData = mutableListOf<LatLng>()
            runCatching {
                tmapRouteUseCase.getRoute(
                    startX = "127.02550910860451",
                    startY = "37.63788539420793",
                    endX = "127.030406594109",
                    endY = "37.609094989686",
                )
            }.onSuccess { response: TmapRoute ->
                Timber.d("${response}")
//                for (route in response.legs) {
//                    when(route.mode){
//                        Mode.WALK.mode -> {
//                            for (lineString in route.steps) {
//                                routeData.addAll(parseLatLng(lineString.lineString))
//                            }
//                        }
//                        else -> {
//                            route.passShape?.let { lineString ->
//                                routeData.addAll(parseLatLng(lineString.linestring))
//                            }
//                        }
//                    }
//                }
            }.onFailure { e->
                Timber.d("$e")
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