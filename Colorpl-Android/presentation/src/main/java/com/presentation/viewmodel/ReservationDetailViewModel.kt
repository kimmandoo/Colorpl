package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.ReservationInfo
import com.domain.usecase.ReservationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReservationDetailViewModel @Inject constructor(
    private val getReservationUseCase: ReservationUseCase,
) : ViewModel() {
    private val _reservationInfo = MutableStateFlow<ReservationInfo>(
        ReservationInfo(
            reservationInfoId = 0,
            contentImg = "",
            title = "",
            category = "",
            runtime = "",
            price = ""
        )
    )
    val reservationInfo: MutableStateFlow<ReservationInfo> = _reservationInfo

    fun setReservationInfo(showId: Int) {
        viewModelScope.launch {
            val data = getReservationUseCase.getReservationInfo(showId)
            _reservationInfo.value = data
            Timber.d(data.toString())
        }
    }
}