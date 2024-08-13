package com.colorpl.show.service;

import com.colorpl.global.common.exception.InvalidRuntimeException;
import com.colorpl.reservation.status.service.GetReservationStatusService;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.GetShowSchedulesRequest;
import com.colorpl.show.dto.GetShowSchedulesResponse;
import com.colorpl.show.dto.GetShowSchedulesResponse.Hall;
import com.colorpl.show.dto.GetShowSchedulesResponse.Timetable;
import com.colorpl.show.repository.ShowDetailRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class GetShowSchedulesService {

    private final ShowDetailRepository showDetailRepository;
    private final GetReservationStatusService getReservationStatusService;
    @Value("${seat.rows}")
    private Integer rows;
    @Value("${seat.cols}")
    private Integer cols;

    public List<GetShowSchedulesResponse> getShowSchedules(Integer showDetailId, LocalDate date) {
        GetShowSchedulesRequest request = GetShowSchedulesRequest.builder()
            .showDetailId(showDetailId).date(date).build();
        ShowDetail showDetail = showDetailRepository.getShowSchedules(request);
        Hall hall = Hall.builder().name(showDetail.getHall().getName()).countSeats(rows * cols)
            .build();
        showDetail.getShowSchedules().forEach(showSchedule -> {
            Integer remainingSeats = (int) getReservationStatusService.getReservationStatus(
                    showSchedule.getId()).getReserved().values().stream()
                .filter(item -> item.getIsReserved().equals(false)).count();
            Timetable timetable = Timetable.builder().showScheduleId(showSchedule.getId())
                .startTime(showSchedule.getDateTime()).endTime(showSchedule.getDateTime()
                    .plus(parse(showSchedule.getShowDetail().getRuntime())))
                .remainingSeats(remainingSeats).build();
            hall.getTimetable().add(timetable);
        });
        GetShowSchedulesResponse response = GetShowSchedulesResponse.builder()
            .name(showDetail.getHall().getTheater().getName()).build();
        response.getHall().add(hall);
        return List.of(response);
    }

    private static Duration parse(String source) {
        Pattern pattern = Pattern.compile("(?:(\\d+)시간)?\\s*(?:(\\d+)분)?");
        Matcher matcher = pattern.matcher(source);
        if (!matcher.find()) {
            throw new InvalidRuntimeException(source);
        }
        String hours = matcher.group(1);
        String minutes = matcher.group(2);
        if (StringUtils.hasText(hours)) {
            if (StringUtils.hasText(minutes)) {
                return Duration.parse("PT%sH%sM".formatted(hours, minutes));
            }
            return Duration.parse("PT%sH".formatted(hours));
        }
        return Duration.parse("PT%sM".formatted(minutes));
    }
}
