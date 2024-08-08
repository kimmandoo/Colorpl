package com.colorpl.show.service;

import com.colorpl.show.dto.SearchShowsRequest;
import com.colorpl.show.dto.SearchShowsResponse;
import com.colorpl.show.repository.ShowDetailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchShowsService {

    private final ShowDetailRepository showDetailRepository;

    @Transactional(readOnly = true)
    public List<SearchShowsResponse> searchShows(SearchShowsRequest request) {
        return showDetailRepository.searchShows(request).stream()
            .map(SearchShowsResponse::from)
            .toList();
    }
}
