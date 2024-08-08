package com.colorpl.show.repository;

import com.colorpl.show.domain.schedule.ShowSchedule;
import com.colorpl.show.dto.SearchShowScheduleCondition;
import java.util.List;

public interface ShowScheduleRepositoryCustom {

    List<ShowSchedule> search(SearchShowScheduleCondition condition);
}
