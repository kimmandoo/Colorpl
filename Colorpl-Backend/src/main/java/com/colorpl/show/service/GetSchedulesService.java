package com.colorpl.show.service;

import com.colorpl.global.common.exception.InvalidRuntimeException;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.GetShowDetailAndHallAndTheaterAndShowSchedulesByCondition;
import com.colorpl.show.repository.ShowDetailRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSchedulesService {

    private final ShowDetailRepository showDetailRepository;

    public List<GetSchedulesService> getSchedules(Integer showDetailId, LocalDate date) {
        GetShowDetailAndHallAndTheaterAndShowSchedulesByCondition condition
            = GetShowDetailAndHallAndTheaterAndShowSchedulesByCondition.builder()
            .showDetailId(showDetailId)
            .date(date)
            .build();
        ShowDetail showDetail = showDetailRepository.getShowDetailAndHallAndTheaterAndShowSchedules(
            condition);
    }

//    public List<GetSchedulesResponse> getSchedules(Long showDetailId, LocalDate date) {
//
//        ShowDetail showDetail = showDetailRepository.findById(showDetailId).orElseThrow();
//        LocalDateTime from = date.atStartOfDay();
//        LocalDateTime to = from.plusDays(1);
//
//        Duration duration = getDuration(showDetail);
//
//        GetShowDetailAndHallAndTheaterAndShowSchedulesByCondition condition = GetShowDetailAndHallAndTheaterAndShowSchedulesByCondition.builder()
//            .showDetail(showDetail).from(from).to(to).build();
//
//        List<ShowSchedule> showSchedules = showScheduleRepository.search(condition);
//
//        List<Timetable> timetable = showSchedules.stream().map(
//            showSchedule -> Timetable.builder().showScheduleId(showSchedule.getId())
//                .startTime(showSchedule.getDateTime())
//                .endTime(showSchedule.getDateTime().plus(duration)).remainingSeats(
//                    (int) getReservationStatusService.getReservationStatus(showSchedule.getId())
//                        .getReserved().values().stream()
//                        .filter(b -> b.getIsReserved().equals(false)).count()).build()).toList();
//
//        Hall hall = Hall.builder().name(showDetail.getHall().getName()).countSeats(rows * cols)
//            .timetable(timetable).build();
//
//        GetSchedulesResponse response = GetSchedulesResponse.builder()
//            .name(showDetail.getHall().getTheater().getName()).build();
//
//        response.getHall().add(hall);
//
//        return List.of(response);
//    }

    private static Duration parse(String source) {
        Pattern pattern = Pattern.compile("(\\d+)시간\\s+(\\d+)분");
        Matcher matcher = pattern.matcher(source);
        if (!matcher.find()) {
            throw new InvalidRuntimeException(source);
        }
        return Duration.parse("PT%sH%sM".formatted(matcher.group(1), matcher.group(2)));
    }
}
