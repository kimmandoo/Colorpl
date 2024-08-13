package com.colorpl.schedule.repository;

import static com.colorpl.reservation.domain.QReservation.*;
import static com.colorpl.reservation.domain.QReservationDetail.reservationDetail;
import static com.colorpl.schedule.domain.QReservationSchedule.reservationSchedule;
import static com.colorpl.show.domain.QShowDetail.showDetail;
import static com.colorpl.show.domain.QShowSchedule.showSchedule;

import com.colorpl.member.Member;
import com.colorpl.reservation.domain.QReservation;
import com.colorpl.schedule.domain.ReservationSchedule;
import com.colorpl.schedule.dto.SearchScheduleCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReservationScheduleRepositoryImpl implements ReservationScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReservationSchedule> search(SearchScheduleCondition condition) {
        return queryFactory
            .select(reservationSchedule)
            .from(reservationSchedule)
            .join(reservationSchedule.reservation, reservation).fetchJoin()
            .join(reservation.reservationDetails, reservationDetail).fetchJoin()
            .join(reservationDetail.showSchedule, showSchedule).fetchJoin()
            .join(showSchedule.showDetail, showDetail).fetchJoin()
            .where(
                memberEq(condition.getMember()),
                dateTimeBetween(condition.getFrom(), condition.getTo())
            )
            .fetch();
    }

    private BooleanExpression memberEq(Member member) {
        return member != null ? reservationSchedule.member.eq(member) : null;
    }

    private BooleanExpression dateTimeBetween(LocalDateTime from, LocalDateTime to) {
        return from != null && to != null ? showSchedule.dateTime.between(from, to) : null;
    }
}
