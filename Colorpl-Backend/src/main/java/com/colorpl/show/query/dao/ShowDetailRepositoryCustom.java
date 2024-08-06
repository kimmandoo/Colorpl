package com.colorpl.show.query.dao;

import com.colorpl.show.domain.detail.ShowDetail;
import java.util.List;

public interface ShowDetailRepositoryCustom {

    List<ShowDetail> showDetailList(ShowDetailSearchCondition condition);
}
