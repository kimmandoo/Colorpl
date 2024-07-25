package com.colorpl.show.domain.schedule.application;

import com.colorpl.show.domain.show.domain.Show;
import com.colorpl.show.domain.schedule.domain.ShowSchedule;
import com.colorpl.show.domain.schedule.domain.ShowScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateShowScheduleService {

    private final ShowScheduleRepository showScheduleRepository;

    public List<Integer> createShowSchedules(Show show, String dtguidance) {
        List<Integer> ids = new ArrayList<>();
        Map<DayOfWeek, List<LocalTime>> schedules = convertStringToSchedules(dtguidance);
        for (LocalDate date = show.getStartDate(); date.isBefore(show.getEndDate()) || date.isEqual(show.getEndDate()); date = date.plusDays(1)) {
            for (LocalTime time : schedules.getOrDefault(date.getDayOfWeek(), new ArrayList<>())) {
                ShowSchedule showSchedule = createShowSchedule(show, date, time);
                showScheduleRepository.save(showSchedule);
                ids.add(showSchedule.getId());
            }
        }
        return ids;
    }

    private ShowSchedule createShowSchedule(Show show, LocalDate date, LocalTime time) {
        return ShowSchedule.builder().show(show).date(LocalDateTime.of(date, time)).build();
    }

    private Map<DayOfWeek, List<LocalTime>> convertStringToSchedules(String source) {

        Map<DayOfWeek, List<LocalTime>> schedules = new HashMap<>();

        for (String s : source.split(", ")) {

            int index = s.indexOf('(');
            String[] dayOfWeek = s.substring(0, index).split(" ~ ");
            List<LocalTime> times = Arrays.stream(s.substring(index + 1, s.length() - 1).split(",")).map(this::convertStringToTime).toList();

            switch (dayOfWeek.length) {
                case 1 -> schedules.put(convertStringToDayOfWeek(dayOfWeek[0]), times);
                case 2 -> {

                    DayOfWeek from = convertStringToDayOfWeek(dayOfWeek[0]);
                    DayOfWeek to = convertStringToDayOfWeek(dayOfWeek[1]);

                    for (int i = from.getValue(); i <= to.getValue(); i++)
                        schedules.put(DayOfWeek.of(i), times);
                }
                default -> throw new IllegalArgumentException("Invalid day of week: " + Arrays.toString(dayOfWeek));
            }
        }
        return schedules;
    }

    private DayOfWeek convertStringToDayOfWeek(String source) {
        return switch (source) {
            case "월요일" -> DayOfWeek.MONDAY;
            case "화요일" -> DayOfWeek.TUESDAY;
            case "수요일" -> DayOfWeek.WEDNESDAY;
            case "목요일" -> DayOfWeek.THURSDAY;
            case "금요일" -> DayOfWeek.FRIDAY;
            case "토요일" -> DayOfWeek.SATURDAY;
            case "일요일" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("Invalid day of week: " + source);
        };
    }

    private LocalTime convertStringToTime(String source) {
        return LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm"));
    }

}
