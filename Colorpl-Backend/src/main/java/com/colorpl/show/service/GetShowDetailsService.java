package com.colorpl.show.service;

import com.colorpl.show.domain.Area;
import com.colorpl.show.domain.Category;
import com.colorpl.show.dto.GetShowDetailsResponse;
import com.colorpl.show.repository.ShowDetailRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetShowDetailsService {

    private final ShowDetailRepository showDetailRepository;

    public List<GetShowDetailsResponse> getShowDetails(
        LocalDate date,
        String keyword,
        List<Area> area,
        Category category,
        Integer cursorId,
        int limit
    ) {
        return showDetailRepository.getShowDetails(
                date,
                keyword,
                area,
                category,
                cursorId,
                limit
            )
            .stream()
            .map(GetShowDetailsResponse::from)
            .toList();
    }
}
