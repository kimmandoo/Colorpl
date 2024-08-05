package com.colorpl.schedule.query.dao;

import com.colorpl.member.Member;
import com.colorpl.schedule.command.domain.ReservationSchedule;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {

    List<ReservationSchedule> monthlyReservationScheduleList(Member member, LocalDateTime from,
        LocalDateTime to);
}
