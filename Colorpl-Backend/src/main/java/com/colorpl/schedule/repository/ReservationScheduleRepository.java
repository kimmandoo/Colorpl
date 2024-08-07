package com.colorpl.schedule.repository;

import com.colorpl.schedule.domain.ReservationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationScheduleRepository extends JpaRepository<ReservationSchedule, Long>,
    ReservationScheduleRepositoryCustom {

}
