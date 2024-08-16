package com.colorpl.schedule.repository;

import com.colorpl.schedule.domain.ReservationSchedule;
import com.colorpl.schedule.dto.SearchScheduleCondition;
import java.util.List;

public interface ReservationScheduleRepositoryCustom {

    List<ReservationSchedule> search(SearchScheduleCondition condition);

    ReservationSchedule getSchedule(Long id);
}
