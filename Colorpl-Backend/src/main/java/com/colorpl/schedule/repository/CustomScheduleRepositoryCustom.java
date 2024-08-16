package com.colorpl.schedule.repository;

import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.dto.SearchScheduleCondition;
import java.util.List;

public interface CustomScheduleRepositoryCustom {

    List<CustomSchedule> search(SearchScheduleCondition condition);
}
