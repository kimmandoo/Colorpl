package com.colorpl.show.repository;

import com.colorpl.show.domain.ShowSchedule;
import java.util.List;

public interface ShowScheduleRepositoryCustom {

    void batchInsert(List<ShowSchedule> showSchedules);
}
