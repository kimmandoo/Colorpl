package com.colorpl.show.service;

import com.colorpl.show.domain.detail.ShowDetail;
import com.colorpl.show.dto.SearchShowDetailCondition;
import com.colorpl.show.dto.SeatListResponse;
import com.colorpl.show.dto.ShowDetailListResponse;
import com.colorpl.show.dto.ShowDetailResponse;
import com.colorpl.show.repository.SeatRepository;
import com.colorpl.show.repository.ShowDetailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchShowDetailService {

    private final ShowDetailRepository showDetailRepository;
    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public List<ShowDetailListResponse> search(SearchShowDetailCondition condition) {
        return showDetailRepository.search(condition).stream()
            .map(ShowDetailListResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public ShowDetailResponse getShowDetail(Long showId) {
        ShowDetail showDetail = showDetailRepository.findById(showId).orElseThrow();
        List<SeatListResponse> seats = seatRepository.findByShowDetail(showDetail).stream()
            .map(SeatListResponse::from).toList();
        return ShowDetailResponse.builder()
            .id(showDetail.getId())
            .apiId(showDetail.getApiId())
            .name(showDetail.getName())
            .cast(showDetail.getCast())
            .runtime(showDetail.getRuntime())
            .priceBySeatClass(showDetail.getPriceBySeatClass())
            .posterImagePath(showDetail.getPosterImagePath())
            .area(showDetail.getArea())
            .category(showDetail.getCategory())
            .state(showDetail.getState())
            .seats(seats)
            .build();
    }
}
