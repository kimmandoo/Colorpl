package com.colorpl.show.service;

import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.GetSchedulesResponse;
import com.colorpl.show.dto.HallListResponse;
import com.colorpl.show.dto.SearchShowScheduleCondition;
import com.colorpl.show.dto.ShowScheduleListItem;
import com.colorpl.show.repository.ShowDetailRepository;
import com.colorpl.show.repository.ShowScheduleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetSchedulesService {

    private final ShowDetailRepository showDetailRepository;
    private final ShowScheduleRepository showScheduleRepository;

    @Value("${seat.rows}")
    private int rows;
    @Value("${seat.cols}")
    private int cols;

    public GetSchedulesResponse getSchedules(Long showDetailId, LocalDate date) {

        ShowDetail showDetail = showDetailRepository.findById(showDetailId).orElseThrow();
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = from.plusDays(1);

        SearchShowScheduleCondition condition = SearchShowScheduleCondition.builder()
            .showDetail(showDetail)
            .from(from)
            .to(to)
            .build();

        List<ShowScheduleListItem> timetable = showScheduleRepository.search(condition).stream()
            .map(showSchedule -> {

                // 남은 좌석 수

                return ShowScheduleListItem.builder()
                    .startTime(showSchedule.getDateTime())
                    .endTime(showSchedule.getDateTime())
                    .build();
            })
            .toList();

        HallListResponse hallListResponse = HallListResponse.builder()
            .name(showDetail.getHall())
            .countSeats(rows * cols)
            .timetable(timetable)
            .build();

        return GetSchedulesResponse.builder()
            .name(null)
            .hall(hallListResponse)
            .build();
    }
}
