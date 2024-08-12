package com.colorpl.show.repository;

import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.GetShowSchedulesRequest;
import com.colorpl.show.dto.GetShowDetailsRequest;
import java.util.List;

public interface ShowDetailRepositoryCustom {

    List<ShowDetail> getShowDetails(GetShowDetailsRequest request);

    ShowDetail getShowDetail(Integer showDetailId);

    ShowDetail getShowSchedules(GetShowSchedulesRequest request);
}
