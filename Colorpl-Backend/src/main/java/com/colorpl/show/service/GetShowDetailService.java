package com.colorpl.show.service;

import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.GetShowDetailResponse;
import com.colorpl.show.repository.ShowDetailRepository;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetShowDetailService {

    private final ShowDetailRepository showDetailRepository;

    @Transactional(readOnly = true)
    public GetShowDetailResponse getShowDetail(Integer showDetailId) {
        ShowDetail showDetail =
            showDetailRepository.findShowDetailAndShowSchedulesById(showDetailId);
        Set<LocalDate> dateTimes = showDetail.getShowSchedules().stream()
            .map(showSchedule -> showSchedule.getDateTime().toLocalDate())
            .collect(Collectors.toSet());
        Map<LocalDate, Boolean> schedule = Stream
            .iterate(LocalDate.now(), date -> date.plusDays(1))
            .limit(14)
            .collect(Collectors.toMap(date -> date, dateTimes::contains));
        return GetShowDetailResponse.of(showDetail, schedule);
    }
}
