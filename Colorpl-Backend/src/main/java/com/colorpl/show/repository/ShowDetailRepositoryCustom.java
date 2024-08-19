package com.colorpl.show.repository;

import com.colorpl.show.domain.Area;
import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.GetShowSchedulesRequest;
import java.time.LocalDate;
import java.util.List;

public interface ShowDetailRepositoryCustom {

    List<ShowDetail> getShowsByCondition(LocalDate date, String keyword, List<Area> area,
        Category category, Integer cursorId, Long limit);

    ShowDetail getShowDetail(Integer showDetailId);

    ShowDetail getShowSchedules(GetShowSchedulesRequest request);

    List<ShowDetail> getShowsByConditionV2(LocalDate date, String keyword, List<Area> area,
        Category category, Integer cursorId, Long limit);
}
