package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Member
import com.domain.model.TicketResponse
import com.domain.usecase.TicketUseCase
import com.domain.usecaseimpl.member.GetMemberInfoUseCase
import com.domain.util.DomainResult
import com.presentation.my_page.model.MemberUiState
import com.presentation.my_page.model.MyPageEventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val ticketUseCase: TicketUseCase
) : ViewModel() {


    private val _memberUiState = MutableStateFlow(MemberUiState())
    val memberUiState: StateFlow<MemberUiState> get() = _memberUiState

    fun setMemberInfo(value: Member) {
        _memberUiState.update { member ->
            member.copy(
                memberInfo = value
            )
        }
    }


    fun getMemberInfo() {
        viewModelScope.launch {
            getMemberInfoUseCase.getMemberInfo().collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        Timber.d("멤버 가져오기 성공 ${result.data}")
                        setMemberInfo(result.data)
                    }

                    is DomainResult.Error -> {
                        Timber.d("멤버 네트워크 오류 ${result.exception}")
                    }
                }
            }
        }
    }

    private val _unUseTickets = MutableStateFlow<List<TicketResponse>>(emptyList())
    val unUseTickets: StateFlow<List<TicketResponse>> = _unUseTickets

    fun setUnUseTickets(value: List<TicketResponse>) {
        _unUseTickets.value = value.toList()
    }

    private val _useTickets = MutableStateFlow<List<TicketResponse>>(emptyList())
    val useTickets: StateFlow<List<TicketResponse>> get() = _useTickets

    fun setUseTickets(value: List<TicketResponse>) {
        _useTickets.value = value.toList()
    }

    private val _myPageEventState = MutableSharedFlow<MyPageEventState>()
    val myPageEventState: SharedFlow<MyPageEventState> get() = _myPageEventState


    init {
        getAllTicket()
    }

    fun getAllTicket() {
        viewModelScope.launch {
            ticketUseCase.getAllTicket().collect {
                when (it) {
                    is DomainResult.Success -> {
                        setDateFilter(it.data)
                        Timber.tag("tickets").d("${it.data}")
                    }

                    is DomainResult.Error -> {
                        Timber.tag("tickets").d("${it.exception}")
                    }
                }
            }
        }
    }

    fun ticketEvent(type: Boolean) {
        viewModelScope.launch {
            if (type) {
                _myPageEventState.emit(MyPageEventState.UseTicket(useTickets.value))
            } else {
                _myPageEventState.emit(MyPageEventState.UnUseTicket(unUseTickets.value))
            }
        }
    }

    fun setDateFilter(list: List<TicketResponse>) { // 시간에 따라 사용, 미사용 분류

        val formatter = DateTimeFormatter.ISO_DATE_TIME

        val nowDate = Instant.now()
        setUseTickets(list.filter {
            ZonedDateTime.parse(it.dateTime + "Z", formatter).toInstant().isBefore(nowDate)
        })
        setUnUseTickets(list.filter {
            ZonedDateTime.parse(it.dateTime + "Z", formatter).toInstant().isAfter(nowDate)
        })
    }


}