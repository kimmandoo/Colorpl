package com.colorpl.ticket.query;

import static com.colorpl.ticket.domain.QTicket.ticket;

import com.colorpl.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;


public class TicketRepositoryImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TicketRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MonthlyTicketListResponse> monthlyTicketList(Member member, LocalDate from,
        LocalDate to) {
        return queryFactory
            .select(new QMonthlyTicketListResponse(
                ticket.id,
                ticket.name,
                ticket.dateTime))
            .from(ticket)
            .where(ticket.member.eq(member),
                ticket.dateTime.between(from.atStartOfDay(), to.plusDays(1).atStartOfDay()))
            .fetch();
    }
}
