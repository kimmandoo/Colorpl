package com.colorpl.show.service;

import com.colorpl.show.dto.SearchShowDetailCondition;
import com.colorpl.show.dto.ShowDetailListResponse;
import com.colorpl.show.repository.ShowDetailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchShowDetailService {

    private final ShowDetailRepository showDetailRepository;

    @Transactional(readOnly = true)
    public List<ShowDetailListResponse> search(SearchShowDetailCondition condition) {
        return showDetailRepository.search(condition).stream()
            .map(ShowDetailListResponse::from)
            .toList();
    }
}
