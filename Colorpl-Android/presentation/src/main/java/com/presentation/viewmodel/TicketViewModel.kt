package com.presentation.viewmodel

import TmapRoute
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.usecase.TmapRouteUseCase
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
            runCatching {
                tmapRouteUseCase.getRoute(
                    startX = "127.02550910860451",
                    startY = "37.63788539420793",
                    endX = "127.030406594109",
                    endY = "37.609094989686",
                )
            }.onSuccess { response: TmapRoute ->
                Timber.d("${response}")
            }.onFailure { e->
                Timber.d("$e")
            }

        }
    }
}