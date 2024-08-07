package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.ReservationInfo
import com.domain.usecase.ReservationListUseCase
import com.domain.util.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReservationListViewModel @Inject constructor(
    private val getReservationListUseCase: ReservationListUseCase,
) : ViewModel() {
    private val _reservationList = MutableStateFlow<List<ReservationInfo>>(listOf())
    val reservationList: MutableStateFlow<List<ReservationInfo>> = _reservationList

    /** 예매 아이템 전체 조회. */
    fun getReservationList() {
        viewModelScope.launch {
            getReservationListUseCase().collect { response ->
                when (response) {
                    is DomainResult.Success -> {
                        _reservationList.value = response.data
                    }

                    is DomainResult.Error -> {
                        Timber.d("예매 정보 조회 실패")
                    }
                }
            }
        }
    }
}