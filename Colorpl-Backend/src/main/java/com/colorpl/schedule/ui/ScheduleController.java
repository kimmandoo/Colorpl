package com.colorpl.schedule.ui;

import com.colorpl.schedule.command.application.CreateCustomScheduleRequest;
import com.colorpl.schedule.command.application.CreateCustomScheduleService;
import com.colorpl.schedule.command.application.CreateReservationScheduleRequest;
import com.colorpl.schedule.command.application.CreateReservationScheduleService;
import com.colorpl.schedule.query.application.MonthlyScheduleListService;
import com.colorpl.schedule.query.application.ScheduleListService;
import com.colorpl.schedule.query.application.MonthlyScheduleListRequest;
import com.colorpl.schedule.query.dto.ScheduleListResponse;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("/schedules")
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final CreateCustomScheduleService createCustomScheduleService;
    private final CreateReservationScheduleService createReservationScheduleService;
    private final MonthlyScheduleListService monthlyScheduleListService;
    private final ScheduleListService scheduleListService;

    @GetMapping("/")
    public ResponseEntity<List<ScheduleListResponse>> scheduleList() {
        return ResponseEntity.ok(scheduleListService.scheduleList());
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<ScheduleListResponse>> monthlyScheduleList(
        @ModelAttribute MonthlyScheduleListRequest request) {

        LocalDateTime from = request.getDate().withDayOfMonth(1).atStartOfDay();
        LocalDateTime to = from.plusMonths(1);

        return ResponseEntity.ok(
            monthlyScheduleListService.monthlyScheduleList(request.getMemberId(), from, to));
    }

    @PostMapping("/custom")
    public ResponseEntity<Long> createCustomSchedule(CreateCustomScheduleRequest request) {

        Long scheduleId = createCustomScheduleService.createCustomSchedule(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/schedules/{scheduleId}")
            .buildAndExpand(scheduleId)
            .toUri();

        return ResponseEntity.created(uri).body(scheduleId);
    }

    @PostMapping("/reservation")
    public ResponseEntity<Long> createReservationSchedule(
        CreateReservationScheduleRequest request) {

        Long scheduleId = createReservationScheduleService.createReservationSchedule(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/schedules/{scheduleId}")
            .buildAndExpand(scheduleId)
            .toUri();

        return ResponseEntity.created(uri).body(scheduleId);
    }
}
