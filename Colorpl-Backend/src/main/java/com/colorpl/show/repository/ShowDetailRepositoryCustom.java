package com.colorpl.show.repository;

import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.SearchShowsRequest;
import java.util.List;

public interface ShowDetailRepositoryCustom {

    List<ShowDetail> searchShows(SearchShowsRequest request);

    ShowDetail findShowDetailAndSeatsById(Integer id);
}
