package com.colorpl.show.service;

import com.colorpl.show.dto.GetShowDetailsByConditionRequest;
import com.colorpl.show.dto.GetShowDetailsByConditionResponse;
import com.colorpl.show.repository.ShowDetailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetShowDetailsByConditionService {

    private final ShowDetailRepository showDetailRepository;

    public List<GetShowDetailsByConditionResponse> getShowDetailsByCondition(
        GetShowDetailsByConditionRequest request) {
        return showDetailRepository.getShowDetailsByCondition(request).stream()
            .map(GetShowDetailsByConditionResponse::from)
            .toList();
    }
}
