package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Description
import com.domain.model.Ticket
import com.domain.usecase.GeocodingUseCase
import com.domain.usecase.OpenAiUseCase
import com.domain.usecase.TicketUseCase
import com.domain.util.DomainResult
import com.naver.maps.geometry.LatLng
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

@HiltViewModel
class TicketCreateViewModel @Inject constructor(
    private val openAiUseCase: OpenAiUseCase,
    private val ticketUseCase: TicketUseCase,
    private val geocodingUseCase: GeocodingUseCase
) : ViewModel() {
    private val _description = MutableStateFlow<Description?>(null)
    val description: StateFlow<Description?> = _description
    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category
    private val _geocodingLatLng = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val geocodingLatLng: StateFlow<LatLng> = _geocodingLatLng
    private val _createResponse = MutableSharedFlow<Int>()
    val createResponse: SharedFlow<Int> = _createResponse.asSharedFlow()

    fun setCategory(text: String) {
        _category.value = text
    }

    fun getAddress(address: String) {
        viewModelScope.launch {
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

    fun cancelGetAddress() {
        viewModelScope.launch {
            _geocodingLatLng.value = LatLng(0.0, 0.0)
        }
    }


    fun createTicket(image: File) {
        viewModelScope.launch {
            _description.value?.let { ticket ->
                ticketUseCase.createTicket(
                    image, Ticket(
                        name = ticket.title,
                        location = ticket.detail,
                        dateTime = ticket.schedule,
                        seat = ticket.seat!!,
                        category = _category.value,
                        latitude = _geocodingLatLng.value.latitude,
                        longitude = _geocodingLatLng.value.longitude
                    )
                ).collectLatest { response ->
                    when (response) {
                        is DomainResult.Success -> {
                            _createResponse.emit(response.data)
                        }

                        is DomainResult.Error -> {
                            _createResponse.emit(-1)
                        }
                    }
                }
            }
        }
    }

    fun getDescription(base64String: String) {
        viewModelScope.launch {
            openAiUseCase(base64String).collectLatest {
                when (it) {
                    is DomainResult.Success -> {
                        _description.value = it.data
                    }

                    is DomainResult.Error -> {

                    }
                }
            }
        }
    }

    fun setTicketInfo(description: Description) {
        _description.value = description
    }

}