package com.colorpl.schedule.controller;

import com.colorpl.schedule.dto.CreateCustomScheduleRequest;
import com.colorpl.schedule.dto.GetScheduleResponse;
import com.colorpl.schedule.dto.ScheduleListResponse;
import com.colorpl.schedule.service.CreateCustomScheduleService;
import com.colorpl.schedule.service.GetScheduleService;
import com.colorpl.schedule.service.SearchScheduleService;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final SearchScheduleService searchScheduleService;
    private final CreateCustomScheduleService createCustomScheduleService;
    private final GetScheduleService getScheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleListResponse>> search() {
        return ResponseEntity.ok(searchScheduleService.search());
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<ScheduleListResponse>> searchMonthly(LocalDate date) {
        return ResponseEntity.ok(searchScheduleService.searchMonthly(date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetScheduleResponse> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(getScheduleService.getSchedule(id));
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
