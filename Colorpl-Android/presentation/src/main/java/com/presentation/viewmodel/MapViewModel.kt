package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.usecase.TicketUseCase
import com.domain.util.DomainResult
import com.presentation.map.model.MapMarker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getTicketUesCase: TicketUseCase,
) : ViewModel() {
    private val _ticketList = MutableStateFlow<List<MapMarker>>(listOf())
    val ticketList: MutableStateFlow<List<MapMarker>> = _ticketList

    fun getTicketList() {
        viewModelScope.launch {
            getTicketUesCase.getAllTicket().collect { response ->
                when (response) {
                    is DomainResult.Success -> {
                        _ticketList.value = response.data.map {
                            MapMarker(
                                id = it.id,
                                image = it.imgUrl,
                                latitude = it.latitude,
                                longitude = it.longitude,
                            )
                        }
                        Timber.tag(this::class.java.simpleName).d("티켓 리스트 조회 성공 : ${ticketList.value}")
                    }
                    is DomainResult.Error -> {
                        Timber.tag(this::class.java.simpleName).d("티켓 리스트 조회 실패")
                    }
                }

            }

        }
    }
}