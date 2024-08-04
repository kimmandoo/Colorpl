package com.colorpl.ticket.query;

import com.colorpl.member.Member;
import java.time.LocalDate;
import java.util.List;

public interface TicketRepositoryCustom {

    List<MonthlyTicketListResponse> monthlyTicketList(Member member, LocalDate from, LocalDate to);
}
