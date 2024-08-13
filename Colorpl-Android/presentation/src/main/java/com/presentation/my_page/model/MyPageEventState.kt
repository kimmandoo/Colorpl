package com.presentation.my_page.model

import com.domain.model.MemberSearch
import com.domain.model.TicketResponse

interface MyPageEventState {

    data class UnUseTicket(val data: List<TicketResponse>) : MyPageEventState
    data class UseTicket(val data: List<TicketResponse>) : MyPageEventState
    data object MemberSearchError : MyPageEventState
    data class MemberSearchSuccess(val data : List<MemberSearch>): MyPageEventState
}