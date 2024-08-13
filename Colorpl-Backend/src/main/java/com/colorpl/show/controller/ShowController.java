package com.colorpl.show.controller;

import com.colorpl.show.dto.CreateShowByApiIdRequest;
import com.colorpl.show.dto.CreateShowByDateRequest;
import com.colorpl.show.dto.GetShowDetailResponse;
import com.colorpl.show.dto.GetShowDetailsRequest;
import com.colorpl.show.dto.GetShowDetailsResponse;
import com.colorpl.show.dto.GetShowSchedulesResponse;
import com.colorpl.show.service.CreateShowService;
import com.colorpl.show.service.GetShowDetailService;
import com.colorpl.show.service.GetShowDetailsService;
import com.colorpl.show.service.GetShowScheduleService;
import com.colorpl.show.service.GetShowSchedulesService;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowController {

    private final GetShowDetailsService getShowDetailsService;
    private final GetShowDetailService getShowDetailService;
    private final GetShowSchedulesService getShowSchedulesService;
    private final GetShowScheduleService getShowScheduleService;
    private final CreateShowService createShowService;

    @GetMapping
    public ResponseEntity<List<GetShowDetailsResponse>> getShowDetails(
        @ModelAttribute GetShowDetailsRequest request
    ) {
        return ResponseEntity
            .ok(getShowDetailsService.getShowDetails(request));
    }

    @GetMapping("/{showDetailId}")
    public ResponseEntity<GetShowDetailResponse> getShowDetail(@PathVariable Integer showDetailId) {
        return ResponseEntity.ok(getShowDetailService.getShowDetail(showDetailId));
    }

    @GetMapping("/{showDetailId}/schedules")
    public ResponseEntity<List<GetShowSchedulesResponse>> getShowSchedules(
        @PathVariable Integer showDetailId,
        @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(getShowSchedulesService.getShowSchedules(showDetailId, date));
    }

    @GetMapping("/{showDetailId}/schedules/{showScheduleId}")
    public ResponseEntity<?> getShowSchedule(
        @PathVariable Integer showDetailId,
        @PathVariable Long showScheduleId
    ) {
        return ResponseEntity.ok(getShowScheduleService.getShowSchedule(showScheduleId));
    }

    @PostMapping("/createShowByApiId")
    public ResponseEntity<Integer> createShowByApiId(
        @RequestBody CreateShowByApiIdRequest request
    ) {
        Integer showDetailId = createShowService.createShowByApiId(request.getApiId());
        return ResponseEntity.created(getLocation(showDetailId)).body(showDetailId);
    }

    @PostMapping("/createShowByDate")
    public ResponseEntity<Void> createShowByDate(
        @RequestBody CreateShowByDateRequest request
    ) {
        createShowService.createShowByDate(request.getFrom(), request.getTo());
        return ResponseEntity.ok().build();
    }

    private URI getLocation(Integer showDetailId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/shows")
            .path("/{showDetailId}")
            .buildAndExpand(showDetailId)
            .toUri();
    }
}
