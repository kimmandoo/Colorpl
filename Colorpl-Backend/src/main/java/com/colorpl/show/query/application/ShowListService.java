package com.colorpl.show.query.application;

import com.colorpl.show.domain.detail.ShowDetailRepository;
import com.colorpl.show.query.dto.ShowListResponse;
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
    public List<ShowListResponse> showList() {
        return showDetailRepository.showList().stream()
            .map(ShowListResponse::from)
            .toList();
    }
}
