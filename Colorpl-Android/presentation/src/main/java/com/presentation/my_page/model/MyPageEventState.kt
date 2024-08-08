package com.presentation.my_page.model

import com.domain.model.TicketResponse

interface MyPageEventState {

    data class UnUseTicket(val data: List<TicketResponse>) : MyPageEventState
    data class UseTicket(val data: List<TicketResponse>) : MyPageEventState

}