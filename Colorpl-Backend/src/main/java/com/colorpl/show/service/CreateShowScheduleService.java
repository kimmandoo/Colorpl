package com.colorpl.show.service;

import com.colorpl.global.common.exception.InvalidDayOfWeekException;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.domain.ShowSchedule;
import com.colorpl.show.repository.ShowScheduleRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateShowScheduleService {

    private final ShowScheduleRepository showScheduleRepository;

    @Transactional
    public void createShowSchedule(
        String schedule,
        LocalDate from,
        LocalDate to,
        ShowDetail showDetail
    ) {
        Map<DayOfWeek, List<LocalTime>> schedules = parseSchedule(schedule);
        from.datesUntil(to)
            .filter(date -> schedules.containsKey(date.getDayOfWeek()))
            .forEach(date -> schedules.get(date.getDayOfWeek())
                .forEach(time -> {
                    ShowSchedule showSchedule = ShowSchedule.builder()
                        .showDetail(showDetail)
                        .dateTime(LocalDateTime.of(date, time))
                        .build();
                    showScheduleRepository.save(showSchedule);
                }));
    }

    private Map<DayOfWeek, List<LocalTime>> parseSchedule(String schedule) {
        Map<DayOfWeek, List<LocalTime>> map = new HashMap<>();
        for (String s : schedule.split(", ")) {
            int index = s.lastIndexOf('(');
            String[] daysOfWeek = s.substring(0, index).split(" ~ ");
            List<LocalTime> times = Arrays.stream(s.substring(index + 1, s.length() - 1)
                    .split(","))
                .map(this::parseTime)
                .toList();
            if (daysOfWeek[0].equals("HOL")) {
                map.put(DayOfWeek.SATURDAY, times);
                map.put(DayOfWeek.SUNDAY, times);
                continue;
            }
            switch (daysOfWeek.length) {
                case 1 -> map.put(parseDayOfWeek(daysOfWeek[0]), times);
                case 2 -> {
                    DayOfWeek from = parseDayOfWeek(daysOfWeek[0]);
                    DayOfWeek to = parseDayOfWeek(daysOfWeek[1]);
                    for (int i = from.getValue(); i <= to.getValue(); i++) {
                        map.put(DayOfWeek.of(i), times);
                    }
                }
                default -> throw new IllegalArgumentException(Arrays.toString(daysOfWeek));
            }
        }
        return map;
    }

    private DayOfWeek parseDayOfWeek(String dayOfWeek) {
        return switch (dayOfWeek) {
            case "월요일" -> DayOfWeek.MONDAY;
            case "화요일" -> DayOfWeek.TUESDAY;
            case "수요일" -> DayOfWeek.WEDNESDAY;
            case "목요일" -> DayOfWeek.THURSDAY;
            case "금요일" -> DayOfWeek.FRIDAY;
            case "토요일" -> DayOfWeek.SATURDAY;
            case "일요일" -> DayOfWeek.SUNDAY;
            default -> throw new InvalidDayOfWeekException(dayOfWeek);
        };
    }

    private LocalTime parseTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, formatter);
    }
}
