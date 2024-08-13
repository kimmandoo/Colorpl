package com.colorpl.schedule.controller;

import com.colorpl.schedule.dto.CreateCustomScheduleRequest;
import com.colorpl.schedule.dto.ScheduleListResponse;
import com.colorpl.schedule.service.CreateCustomScheduleService;
import com.colorpl.schedule.service.CreateReservationScheduleService;
import com.colorpl.schedule.service.SearchScheduleService;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("/schedules")
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SearchScheduleService searchScheduleService;
    private final CreateCustomScheduleService createCustomScheduleService;
    private final CreateReservationScheduleService createReservationScheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleListResponse>> search() {
        return ResponseEntity.ok(searchScheduleService.search());
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<ScheduleListResponse>> searchMonthly(LocalDate date) {
        return ResponseEntity.ok(searchScheduleService.searchMonthly(date));
    }

    @PostMapping("/custom")
    public ResponseEntity<Long> createCustomSchedule(
        @RequestPart CreateCustomScheduleRequest request,
        @RequestPart(required = false) MultipartFile file
    ) {
        Long id = createCustomScheduleService.create(request, file);
        return ResponseEntity.created(getLocation(id)).body(id);
    }

    private URI getLocation(Long id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/schedules")
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
    }
}
