package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.TicketResponse
import com.domain.usecase.TicketUseCase
import com.domain.util.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TicketSelectViewModel @Inject constructor(
    private val ticketUseCase: TicketUseCase
) : ViewModel() {
    private val _tickets = MutableSharedFlow<List<TicketResponse>>()
    val tickets: SharedFlow<List<TicketResponse>> = _tickets


    init {
        getUnreviewedTicket()
    }

    private fun getUnreviewedTicket() {
        viewModelScope.launch {
            ticketUseCase.getAllTicket().collect {
                when (it) {
                    is DomainResult.Success -> {
                        _tickets.emit(it.data.filter { it.reviewExists == false })
                        Timber.tag("tickets").d("${it.data}")
                    }

                    is DomainResult.Error -> {
                        Timber.tag("tickets").d("${it.exception}")
                    }
                }
            }
        }
    }

}