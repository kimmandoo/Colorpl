package com.colorpl.show.repository;

import com.colorpl.show.domain.schedule.ShowSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowScheduleRepository extends JpaRepository<ShowSchedule, Integer> {
}