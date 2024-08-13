package com.colorpl.schedule.repository;

import static com.colorpl.reservation.domain.QReservation.reservation;
import static com.colorpl.reservation.domain.QReservationDetail.reservationDetail;
import static com.colorpl.schedule.domain.QReservationSchedule.reservationSchedule;
import static com.colorpl.show.domain.QShowDetail.showDetail;
import static com.colorpl.show.domain.QShowSchedule.showSchedule;
import static com.colorpl.theater.domain.QHall.hall;
import static com.colorpl.theater.domain.QTheater.theater;

import com.colorpl.member.Member;
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

    public ReservationSchedule getSchedule(Long id) {
        return queryFactory
            .select(reservationSchedule)
            .from(reservationSchedule)
            .join(reservationSchedule.reservation, reservation).fetchJoin()
            .join(reservation.reservationDetails, reservationDetail).fetchJoin()
            .join(reservationDetail.showSchedule, showSchedule).fetchJoin()
            .join(showSchedule.showDetail, showDetail).fetchJoin()
            .join(showDetail.hall, hall).fetchJoin()
            .join(hall.theater, theater).fetchJoin()
            .where(reservationSchedule.id.eq(id))
            .fetchOne();
    }

    private BooleanExpression memberEq(Member member) {
        return member != null ? reservationSchedule.member.eq(member) : null;
    }

    private BooleanExpression dateTimeBetween(LocalDateTime from, LocalDateTime to) {
        return from != null && to != null ? showSchedule.dateTime.between(from, to) : null;
    }
}
