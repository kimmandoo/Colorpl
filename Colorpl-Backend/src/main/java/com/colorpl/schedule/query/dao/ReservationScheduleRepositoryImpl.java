package com.colorpl.schedule.query.dao;

import static com.colorpl.reservation.domain.QReservationDetail.reservationDetail;
import static com.colorpl.schedule.command.domain.QReservationSchedule.reservationSchedule;
import static com.colorpl.show.domain.detail.QShowDetail.showDetail;
import static com.colorpl.show.domain.schedule.QShowSchedule.showSchedule;

import com.colorpl.member.Member;
import com.colorpl.schedule.command.domain.ReservationSchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationScheduleRepositoryImpl implements ReservationScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReservationSchedule> monthlyReservationScheduleList(Member member,
        LocalDateTime from, LocalDateTime to) {
        return queryFactory.select(reservationSchedule).from(reservationSchedule)
            .join(reservationSchedule.reservationDetail, reservationDetail).fetchJoin()
            .join(reservationDetail.showSchedule, showSchedule).fetchJoin()
            .join(showSchedule.showDetail, showDetail).fetchJoin()
            .where(reservationSchedule.member.eq(member), showSchedule.dateTime.between(from, to))
            .fetch();
    }
}
