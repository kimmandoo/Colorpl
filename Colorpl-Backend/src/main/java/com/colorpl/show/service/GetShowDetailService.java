package com.colorpl.show.service;

import com.colorpl.global.common.exception.ShowDetailNotFoundException;
import com.colorpl.show.dto.GetShowDetailResponse;
import com.colorpl.show.repository.ShowDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetShowDetailService {

    private final ShowDetailRepository showDetailRepository;

    @Transactional(readOnly = true)
    public GetShowDetailResponse getShowDetail(Long showDetailId) {
        return GetShowDetailResponse.from(showDetailRepository.findById(showDetailId)
            .orElseThrow(ShowDetailNotFoundException::new));
    }
}
