package com.colorpl.schedule.command.domain;

import com.colorpl.schedule.query.dao.ScheduleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>,
    ScheduleRepositoryCustom {

}
