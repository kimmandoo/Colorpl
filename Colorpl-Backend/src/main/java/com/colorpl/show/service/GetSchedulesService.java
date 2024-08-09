package com.colorpl.show.service;

import com.colorpl.global.common.exception.InvalidRuntimeException;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.domain.ShowSchedule;
import com.colorpl.show.dto.GetSchedulesResponse;
import com.colorpl.show.dto.GetSchedulesResponse.Hall;
import com.colorpl.show.dto.GetSchedulesResponse.Hall.Timetable;
import com.colorpl.show.dto.SearchShowScheduleCondition;
import com.colorpl.show.repository.ShowDetailRepository;
import com.colorpl.show.repository.ShowScheduleRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public List<GetSchedulesResponse> getSchedules(Long showDetailId, LocalDate date) {

        ShowDetail showDetail = showDetailRepository.findById(showDetailId).orElseThrow();
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = from.plusDays(1);

        String regex = "(\\d+)시간\\s+(\\d+)분";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(showDetail.getRuntime());

        if (!matcher.find()) {
            throw new InvalidRuntimeException(showDetail.getRuntime());
        }

        String hours = matcher.group(1);
        String minutes = matcher.group(2);
        String string = String.format("PT%sH%sM", hours, minutes);
        Duration duration = Duration.parse(string);

        SearchShowScheduleCondition condition = SearchShowScheduleCondition.builder()
            .showDetail(showDetail)
            .from(from)
            .to(to)
            .build();

        List<ShowSchedule> showSchedules = showScheduleRepository.search(condition);

        List<Timetable> timetable = showSchedules.stream()
            .map(showSchedule -> {

                // 남은 좌석 수

                return Timetable.builder()
                    .startTime(showSchedule.getDateTime())
                    .endTime(showSchedule.getDateTime().plus(duration))
                    .build();
            })
            .toList();

        Hall hall = Hall.builder()
            .name(showDetail.getHall().getName())
            .countSeats(rows * cols)
            .timetable(timetable)
            .build();

        GetSchedulesResponse response = GetSchedulesResponse.builder()
            .name(showDetail.getHall().getTheater().getName())
            .build();

        response.getHall().add(hall);

        return List.of(response);
    }
}
