package com.colorpl.ticket.query;

import static com.colorpl.review.domain.QReview.review;
import static com.colorpl.ticket.domain.QTicket.ticket;

import com.colorpl.member.Member;
import com.colorpl.ticket.domain.Ticket;
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
    public List<Ticket> monthlyTicketList(Member member, LocalDateTime from,
        LocalDateTime to) {
        return queryFactory
            .select(ticket)
            .from(ticket)
            .leftJoin(ticket.review, review).fetchJoin()
            .fetch();
    }
}
