package com.colorpl.schedule.query.dao;

import com.colorpl.member.Member;
import com.colorpl.schedule.command.domain.CustomSchedule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomScheduleRepository extends JpaRepository<CustomSchedule, Long> {

    List<CustomSchedule> findByMemberAndDateTimeBetween(Member member, LocalDateTime from,
        LocalDateTime to);
}
