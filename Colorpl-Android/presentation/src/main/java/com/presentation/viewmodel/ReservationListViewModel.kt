package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.domain.model.ReservationInfo
import com.domain.model.ShowParam
import com.domain.usecase.ReservationListUseCase
import com.domain.util.DomainResult
import com.presentation.util.Area
import com.presentation.util.ShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReservationListViewModel @Inject constructor(
    private val getReservationListUseCase: ReservationListUseCase,
) : ViewModel() {
    private val _pagedShow = MutableStateFlow<PagingData<ReservationInfo>?>(null)
    val pagedShow: MutableStateFlow<PagingData<ReservationInfo>?> = _pagedShow


    private val _showParam = MutableStateFlow<ShowParam>(ShowParam())
    val showParam: StateFlow<ShowParam> get() = _showParam

    fun setShowParam(value: ShowParam) {
        _showParam.value = value
    }

    private val _date = MutableStateFlow<LocalDate?>(null)
    val date : StateFlow<LocalDate?> get() = _date

    fun setDate(value : LocalDate?){
        _date.value = value
    }

    private val _area = MutableStateFlow<List<Area>?>(null)
    val area: StateFlow<List<Area>?> get() = _area

    fun setArea(area: List<Area>?) {
        _area.value = area
    }



    init {
        getReservationList(setFilter())
    }

    fun setParam(type: ShowType, param: String) {
        when (type) {
            ShowType.KEYWORD -> {
                setShowParam(
                    _showParam.value.copy(
                        keyword = param
                    )
                )
            }

            ShowType.DATE -> {
                setShowParam(
                    _showParam.value.copy(
                        date = param
                    )
                )
            }

            ShowType.CATEGORY -> {
                setShowParam(
                    _showParam.value.copy(
                        category = param
                    )
                )
            }

            ShowType.LOCATION -> {
                setShowParam(
                    _showParam.value.copy(
                        area = param
                    )
                )
            }
        }
        getReservationList(filter = setFilter())
    }

    fun setFilter(): Map<String, String?> {
        val data = showParam.value
        Timber.d("파람 확인 $data")
        return hashMapOf(
            "date" to data.date,
            "area" to data.area,
            "keyword" to data.keyword,
            "category" to data.category
        ).filter { it.value?.isNotEmpty() == true }
    }

    fun dataClear(){
        setShowParam(ShowParam())
        getReservationList(setFilter())
        setDate(null)
        setArea(null)
    }



    /** 예매 아이템 전체 조회. */
    fun getReservationList(filter: Map<String, String?>) {
        viewModelScope.launch {
            getReservationListUseCase(filters = filter).collectLatest { pagedData ->
                _pagedShow.value = pagedData
            }
        }
    }

}