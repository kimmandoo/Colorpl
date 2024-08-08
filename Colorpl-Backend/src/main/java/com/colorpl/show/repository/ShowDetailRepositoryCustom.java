package com.colorpl.show.repository;

import com.colorpl.show.domain.detail.ShowDetail;
import com.colorpl.show.dto.SearchShowDetailCondition;
import java.util.List;

public interface ShowDetailRepositoryCustom {

    List<ShowDetail> search(SearchShowDetailCondition condition);
}
