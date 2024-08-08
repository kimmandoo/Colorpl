package com.colorpl.schedule.repository;

import com.colorpl.schedule.domain.CustomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomScheduleRepository extends JpaRepository<CustomSchedule, Long>,
    CustomScheduleRepositoryCustom {

}
