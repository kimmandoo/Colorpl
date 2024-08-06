package com.colorpl.schedule.query.dao;

import com.colorpl.schedule.command.domain.ReservationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationScheduleRepository extends JpaRepository<ReservationSchedule, Long>,
    ReservationScheduleRepositoryCustom {

}
