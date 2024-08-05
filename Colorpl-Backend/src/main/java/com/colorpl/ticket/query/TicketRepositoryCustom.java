package com.colorpl.ticket.query;

import com.colorpl.member.Member;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepositoryCustom {

    List<MonthlyTicketListResponse> monthlyTicketList(Member member, LocalDateTime from,
        LocalDateTime to);
}
