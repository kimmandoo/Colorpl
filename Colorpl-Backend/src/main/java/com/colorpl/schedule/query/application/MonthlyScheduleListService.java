package com.colorpl.schedule.query.application;

import com.colorpl.member.Member;
import com.colorpl.schedule.command.domain.ReservationSchedule;
import com.colorpl.schedule.command.domain.ScheduleRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MonthlyScheduleListService {

    private final ScheduleRepository scheduleRepository;

    public void monthlyScheduleList(Member member, LocalDateTime from, LocalDateTime to) {
        List<ReservationSchedule> reservationSchedules = scheduleRepository.monthlyReservationScheduleList(
            member, from, to);
    }
}
