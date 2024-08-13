package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Description
import com.domain.model.TicketRequest
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
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
    private val _juso = MutableStateFlow("")
    val juso: StateFlow<String> = _juso
    private val _createResponse = MutableSharedFlow<Int>()
    val createResponse: SharedFlow<Int> = _createResponse.asSharedFlow()
    private val _ticketInfo = MutableStateFlow<Boolean>(false)
    val ticketInfo: StateFlow<Boolean> = _ticketInfo
    private val _scheduleInfo = MutableStateFlow<Boolean>(false)
    val scheduleInfo: StateFlow<Boolean> = _scheduleInfo

    fun setCategory(text: String) {
        _category.value = text
        stateConfirm()
    }

    fun setJuso(text:String){
        _juso.value = text
    }

    fun setSchedule(text: String) {
        _scheduleInfo.value = isValidDateFormat(text)
    }

    fun clearCategory() {
        _category.value = ""
        stateConfirm()
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

    fun cancelGetAddress() {
        viewModelScope.launch {
            _geocodingLatLng.value = LatLng(0.0, 0.0)
        }
    }

    private fun stateConfirm() {
        _ticketInfo.value = (_description.value != null && _category.value != "")
    }


    fun createTicket(image: File) {
        viewModelScope.launch {
            _description.value?.let { ticket ->
                ticketUseCase.createTicket(
                    image, TicketRequest(
                        name = ticket.title,
                        location = ticket.detail,
                        dateTime = ticket.schedule,
                        seat = ticket.seat!!,
                        category = _category.value,
                        latitude = _geocodingLatLng.value.latitude,
                        longitude = _geocodingLatLng.value.longitude,
                    )
                ).collectLatest { response ->
                    when (response) {
                        is DomainResult.Success -> {
                            _createResponse.emit(response.data)
                            stateConfirm()
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
                        setSchedule(it.data.schedule)
                        _description.value = it.data
                        stateConfirm()
                    }

                    is DomainResult.Error -> {

                    }
                }
            }
        }
    }

    companion object {
        private const val DATE_PATTERN = "yyyy년 MM월 dd일 HH:mm"
        private val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.KOREAN)

        fun isValidDateFormat(input: String): Boolean {
            return try {
                LocalDateTime.parse(input, formatter)
                true
            } catch (e: DateTimeParseException) {
                false
            }
        }
    }
}