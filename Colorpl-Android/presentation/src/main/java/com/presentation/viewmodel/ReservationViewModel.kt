package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.ReservationPairInfo
import com.domain.model.Seat
import com.domain.model.TimeTable
import com.domain.usecase.ReservationScheduleUseCase
import com.domain.usecase.ReservationSeatUseCase
import com.domain.usecase.ReservationUseCase
import com.domain.util.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val getReservationUseCase: ReservationUseCase,
    private val getReservationScheduleUseCase: ReservationScheduleUseCase,
    private val getReservationSeatUseCase: ReservationSeatUseCase

) : ViewModel() {
    private val _reservationDetailId = MutableStateFlow(-1)
    val reservationDetailId: StateFlow<Int> = _reservationDetailId

    private val _reservationImg = MutableStateFlow("")
    val reservationImg: StateFlow<String> = _reservationImg

    private val _reservationTitle = MutableStateFlow("")
    val reservationTitle: StateFlow<String> = _reservationTitle

    private val _reservationDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val reservationDate: StateFlow<LocalDate> = _reservationDate

    private val _reservationPlace = MutableStateFlow("")
    val reservationPlace: StateFlow<String> = _reservationPlace

    private val _reservationTheater = MutableStateFlow("")
    val reservationTheater: StateFlow<String> = _reservationTheater

    private val _reservationSchedule = MutableStateFlow<Map<String, Boolean>>(mapOf())
    val reservationSchedule: StateFlow<Map<String, Boolean>> = _reservationSchedule

    private val _reservationTimeTable =
        MutableStateFlow<TimeTable>(TimeTable(1, "10:00", "12:00", 50))
    val reservationTimeTable: StateFlow<TimeTable> = _reservationTimeTable

    private val _reservationSeat = MutableStateFlow<List<Seat>>(listOf())
    val reservationSeat: StateFlow<List<Seat>> = _reservationSeat

    private val _reservationSelectedSeat = MutableStateFlow<List<Seat>>(listOf())
    val reservationSelectedSeat: StateFlow<List<Seat>> = _reservationSelectedSeat

    private val _reservationPriceBySeatClass = MutableStateFlow<Map<String, Int>>(mapOf())
    val reservationPriceBySeatClass: StateFlow<Map<String, Int>> = _reservationPriceBySeatClass

    private val _reservationTotalPrice = MutableStateFlow(0)
    val reservationTotalPrice: StateFlow<Int> = _reservationTotalPrice

    private val _getReservationSchedule = MutableStateFlow<List<ReservationPairInfo>>(listOf())
    val getReservationSchedule: StateFlow<List<ReservationPairInfo>> = _getReservationSchedule

    private val _getReservationSeat = MutableStateFlow<Map<String, Boolean>>(mapOf())
    val getReservationSeat: StateFlow<Map<String, Boolean>> = _getReservationSeat

    private val _showPlace = MutableStateFlow("")
    val showPlace: StateFlow<String> = _showPlace


    fun setReservationDetailId(id: Int) {
        _reservationDetailId.value = id
    }

    fun setReservationImg(img: String) {
        _reservationImg.value = img
    }

    fun setReservationTitle(title: String) {
        _reservationTitle.value = title
    }

    fun setReservationDate(date: LocalDate) {
        _reservationDate.value = date
    }

    fun setReservationPlace(place: String) {
        _reservationPlace.value = place
    }

    fun setReservationTheater(theater: String) {
        _reservationTheater.value = theater
    }

    fun setReservationSchedule(schedule: Map<String, Boolean>) {
        _reservationSchedule.value = schedule
    }

    fun setReservationTimeTable(timeTable: TimeTable) {
        _reservationTimeTable.value = timeTable
    }

    fun setReservationSeat(seatList: List<Seat>) {
        _reservationSeat.value = seatList
    }

    fun setReservationSelectedSeat(seatList: List<Seat>) {
        _reservationSelectedSeat.value = seatList
    }

    fun setReservationPriceBySeatClass(priceBySeatClass: Map<String, Int>) {
        _reservationPriceBySeatClass.value = priceBySeatClass
    }

    fun setReservationTotalPrice(totalPrice: Int) {
        _reservationTotalPrice.value = totalPrice
    }

    /**
     * 에매가 가능한 **스케줄** 정보를 가져옴.
     * @param showDetailId 공연 특정 ID.
     * @param date 특정 날짜.
     */
    fun getReservationSchedule(
        showDetailId: Int,
        date: String
    ) {
        viewModelScope.launch {
            getReservationScheduleUseCase(showDetailId, date).collect { response ->
                when (response) {
                    is DomainResult.Success -> {
                        _getReservationSchedule.value = response.data
                        Timber.tag(this.javaClass.simpleName).d("${response.data}")
                                Timber.d("예약 정보 조회 성공")
                    }

                    is DomainResult.Error -> {
                        Timber.d("예약 정보 조회 실패")
                    }
                }
            }
        }
    }

    /**
     * 예매가 가능한 **좌석** 정보를 가져옴.
     * @param showDetailId 공연 특정 ID.
     * @param showScheduleId 스케줄 ID.
     */
    fun getReservationSeat(
        showDetailId: Int,
        showScheduleId: Int
    ) {
        viewModelScope.launch {
            getReservationSeatUseCase(showDetailId, showScheduleId).collect { response ->
                when (response) {
                    is DomainResult.Success -> {
                        _reservationSeat.value = response.data
                        Timber.tag(this.javaClass.simpleName).d("${response.data}")
                        Timber.d("예약 정보 조회 성공")
                    }

                    is DomainResult.Error -> {
                        Timber.d("예약 정보 조회 실패")
                    }
                }

            }
        }
    }

    fun calculateTotalPrice() {
        val totalPrice = _reservationSeat.value
            .filter { it.isSelected } // 선택된 좌석만 필터링
            .sumOf { seat ->
                Timber.tag("sumOf").d("${_reservationPriceBySeatClass.value[seat.grade]}")
                _reservationPriceBySeatClass.value[seat.grade] ?: 0
            }
        _reservationTotalPrice.value = totalPrice
    }

    fun clearReservationSeat() {
        _reservationSeat.value = _reservationSeat.value.map { seat ->
            seat.copy(isSelected = false) // 모든 좌석의 isSelected를 false로 설정
        }
    }
}