package com.colorpl.show.query.application;

import com.colorpl.show.domain.detail.ShowDetailRepository;
import com.colorpl.show.query.dao.ShowDetailSearchCondition;
import com.colorpl.show.query.dto.ShowDetailListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ShowDetailListService {

    private final ShowDetailRepository showDetailRepository;

    @Transactional(readOnly = true)
    public List<ShowDetailListResponse> showDetailList(ShowDetailSearchCondition condition) {
        return showDetailRepository.showDetailList(condition).stream()
            .map(ShowDetailListResponse::from)
            .toList();
    }
}
