package com.colorpl.ticket.query;

import static com.colorpl.ticket.domain.QTicket.ticket;

import com.colorpl.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TicketRepositoryImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TicketRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MonthlyTicketListResponse> monthlyTicketList(Member member, LocalDateTime from,
        LocalDateTime to) {
        return queryFactory
            .select(new QMonthlyTicketListResponse(
                ticket.name,
                ticket.theater,
                ticket.dateTime,
                ticket.seat,
                ticket.category))
            .from(ticket)
            .where(
                ticket.member.eq(member),
                ticket.dateTime.between(from, to))
            .fetch();
    }
}
