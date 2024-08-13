package com.colorpl.show.service;

import com.colorpl.show.dto.GetShowDetailsRequest;
import com.colorpl.show.dto.GetShowDetailsResponse;
import com.colorpl.show.repository.ShowDetailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetShowDetailsService {

    private final ShowDetailRepository showDetailRepository;

    public List<GetShowDetailsResponse> getShowDetails(
        GetShowDetailsRequest request) {
        return showDetailRepository.getShowDetails(request).stream()
            .map(GetShowDetailsResponse::from)
            .toList();
    }
}
