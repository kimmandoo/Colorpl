package com.colorpl.show.controller;

import com.colorpl.show.dto.CreateByApiIdRequest;
import com.colorpl.show.dto.GetShowDetailResponse;
import com.colorpl.show.dto.SearchShowsRequest;
import com.colorpl.show.dto.SearchShowsResponse;
import com.colorpl.show.service.CreateShowService;
import com.colorpl.show.service.GetScheduleService;
import com.colorpl.show.service.GetSchedulesService;
import com.colorpl.show.service.GetShowDetailService;
import com.colorpl.show.service.SearchShowsService;
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

    private final SearchShowsService searchShowsService;
    private final GetShowDetailService getShowDetailService;
    private final GetSchedulesService getSchedulesService;
    private final CreateShowService createShowService;
    private final GetScheduleService getScheduleService;

    @GetMapping
    public ResponseEntity<List<SearchShowsResponse>> searchShows(
        @ModelAttribute SearchShowsRequest request
    ) {
        return ResponseEntity.ok(searchShowsService.searchShows(request));
    }

    @GetMapping("/{showDetailId}")
    public ResponseEntity<GetShowDetailResponse> getShowDetail(
        @PathVariable Long showDetailId
    ) {
        return ResponseEntity.ok(getShowDetailService.getShowDetail(showDetailId));
    }

    @GetMapping("/{showDetailId}/schedules")
    public ResponseEntity<?> getSchedules(
        @PathVariable Long showDetailId,
        @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(getSchedulesService.getSchedules(showDetailId, date));
    }

    @GetMapping("/{showDetailId}/schedules/{showScheduleId}")
    public ResponseEntity<?> getShowSchedule(
        @PathVariable String showDetailId,
        @PathVariable Long showScheduleId
    ) {
        return ResponseEntity.ok(getScheduleService.getSchedule(showScheduleId));
    }

    @PostMapping
    public ResponseEntity<Integer> createByApiId(@RequestBody CreateByApiIdRequest request) {

        Integer id = createShowService.createByApiId(request.getApiId());

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/shows/{id}")
            .buildAndExpand(id)
            .toUri();

        return ResponseEntity.created(uri).body(id);
    }
}
