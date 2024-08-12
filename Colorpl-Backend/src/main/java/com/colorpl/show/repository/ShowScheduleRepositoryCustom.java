package com.colorpl.show.repository;

import com.colorpl.show.domain.ShowSchedule;
import com.colorpl.show.dto.GetShowDetailAndHallAndTheaterAndShowSchedulesByCondition;
import java.util.List;

public interface ShowScheduleRepositoryCustom {

    List<ShowSchedule> getShowDetailAndHallAndTheaterAndShowSchedules(
        GetShowDetailAndHallAndTheaterAndShowSchedulesByCondition condition
    );
}
