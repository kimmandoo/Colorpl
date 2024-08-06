package com.colorpl.ticket.query;

import com.colorpl.member.Member;
import com.colorpl.ticket.domain.Ticket;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepositoryCustom {

    List<Ticket> monthlyTicketList(Member member, LocalDateTime from,
        LocalDateTime to);
}
