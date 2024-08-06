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
public class ShowListService {

    private final ShowDetailRepository showDetailRepository;

    @Transactional(readOnly = true)
    public List<ShowDetailListResponse> showList() {
        return showDetailRepository.showList().stream()
            .map(ShowDetailListResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ShowDetailListResponse> search(ShowDetailSearchCondition condition) {
        return showDetailRepository.search(condition).stream()
            .map(ShowDetailListResponse::from)
            .toList();
    }
}
