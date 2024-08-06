package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.ReservationInfo
import com.domain.usecase.ReservationUseCase
import com.domain.util.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReservationDetailViewModel @Inject constructor(
    private val getReservationUseCase: ReservationUseCase,
) : ViewModel() {
    private val _reservationInfo = MutableStateFlow<ReservationInfo>(ReservationInfo())
    val reservationInfo: MutableStateFlow<ReservationInfo> = _reservationInfo

    /** showId로 예매 아이템 정보 단건 조회.
     * @param showId DB의 showId */
    fun getReservationInfo(showId: Int) {
        viewModelScope.launch {
            getReservationUseCase(showId).collectLatest { response ->
                when (response) {
                    is DomainResult.Success -> {
                        _reservationInfo.value = response.data
                    }
                    is DomainResult.Error -> {
                        Timber.d("예매 정보 조회 실패")
                    }
                }
            }
        }
    }
}