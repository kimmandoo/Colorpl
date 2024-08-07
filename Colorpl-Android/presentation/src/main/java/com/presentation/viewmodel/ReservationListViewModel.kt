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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReservationListViewModel @Inject constructor(
    private val getReservationListUseCase: ReservationListUseCase,
) : ViewModel() {
    private val _reservationList = MutableStateFlow<List<ReservationInfo>>(listOf())
    val reservationList: MutableStateFlow<List<ReservationInfo>> = _reservationList

    private val _searchDate = MutableStateFlow("")
    val searchDate: MutableStateFlow<String> = _searchDate

    private val _searchArea = MutableStateFlow("")
    val searchArea: MutableStateFlow<String> = _searchArea

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: MutableStateFlow<String> = _searchKeyword

    private val _searchCategory = MutableStateFlow("")
    val searchCategory: MutableStateFlow<String> = _searchCategory

    /** 예매 아이템 전체 조회. */
    fun getReservationList() {
        viewModelScope.launch {
            val filters = mutableMapOf<String, String>()
            _searchDate.value.takeIf { it.isNotEmpty() }?.let { filters["date"] = it }
            _searchArea.value.takeIf { it.isNotEmpty() }?.let { filters["area"] = it }
            _searchKeyword.value.takeIf { it.isNotEmpty() }?.let { filters["keyword"] = it }
            _searchCategory.value.takeIf { it.isNotEmpty() }?.let { filters["category"] = it }
            Timber.tag("검색").d("${searchDate.value}, ${searchArea.value}, ${searchKeyword.value}, ${searchCategory.value}")
            getReservationListUseCase(filters = filters).collect { response ->
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

    fun setDate(date: LocalDate) {
        _searchDate.value = date.toString()
    }

    fun setArea(area: String) {
        _searchArea.value = area
    }

    fun setKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun setCategory(category: String) {
        _searchCategory.value = category
    }
}