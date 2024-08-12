package com.colorpl.show.controller;

import com.colorpl.show.dto.GetShowDetailResponse;
import com.colorpl.show.dto.GetShowDetailsByConditionRequest;
import com.colorpl.show.dto.GetShowDetailsByConditionResponse;
import com.colorpl.show.service.GetShowDetailService;
import com.colorpl.show.service.GetShowDetailsByConditionService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowController {

    private final GetShowDetailsByConditionService getShowDetailsByConditionService;
    private final GetShowDetailService getShowDetailService;

    @GetMapping
    public ResponseEntity<List<GetShowDetailsByConditionResponse>> getShowDetailsByCondition(
        @ModelAttribute GetShowDetailsByConditionRequest request
    ) {
        return ResponseEntity
            .ok(getShowDetailsByConditionService.getShowDetailsByCondition(request));
    }

    @GetMapping("/{showDetailId}")
    public ResponseEntity<GetShowDetailResponse> getShowDetail(@PathVariable Integer showDetailId) {
        return ResponseEntity.ok(getShowDetailService.getShowDetail(showDetailId));
    }

    @GetMapping("/{showDetailId}/schedules")
    public ResponseEntity<?> getSchedules(
        @PathVariable Integer showDetailId,
        @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok().build();
    }

//
//    @GetMapping("/{showDetailId}/schedules/{showScheduleId}")
//    public ResponseEntity<?> getShowSchedule(
//        @PathVariable Integer showDetailId,
//        @PathVariable Long showScheduleId
//    ) {
//        return ResponseEntity.ok(getScheduleService.getSchedule(showScheduleId));
//    }
//
//    @DeleteMapping("/{showDetailId}/schedules/{showScheduleId}")
//    public ResponseEntity<?> deleteSchedule(
//        @PathVariable Integer showDetailId,
//        @PathVariable Long showScheduleId
//    ) {
//        deleteReservationStatusService.deleteReservationStatus(showScheduleId);
//        return ResponseEntity.ok().build();
//    }
//
//
//    @PostMapping
//    public ResponseEntity<Integer> createByApiId(@RequestBody CreateByApiIdRequest request) {
//
//        Integer id = createShowService.createByApiId(request.getApiId());
//
//        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
//            .path("/shows/{id}")
//            .buildAndExpand(id)
//            .toUri();
//
//        return ResponseEntity.created(uri).body(id);
//    }
}
