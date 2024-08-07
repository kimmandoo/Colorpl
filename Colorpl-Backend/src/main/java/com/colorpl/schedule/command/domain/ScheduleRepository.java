package com.colorpl.schedule.command.domain;

import com.colorpl.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByMember(Member member);
}
