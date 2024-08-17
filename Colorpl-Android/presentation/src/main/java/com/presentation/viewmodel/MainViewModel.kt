package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.domain.usecaseimpl.notification.NotificationUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {

    private val _reservationDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val reservationDate: StateFlow<LocalDate> = _reservationDate

    fun setReservationDate(date: LocalDate) {
        _reservationDate.value = date
    }


}