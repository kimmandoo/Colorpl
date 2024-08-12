package com.colorpl.show.repository;

import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.GetShowDetailsByConditionRequest;
import java.util.List;

public interface ShowDetailRepositoryCustom {

    List<ShowDetail> getShowDetailsByCondition(GetShowDetailsByConditionRequest request);

    ShowDetail findShowDetailAndSeatsById(Integer id);
}
