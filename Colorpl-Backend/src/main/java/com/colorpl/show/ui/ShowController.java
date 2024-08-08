package com.colorpl.show.ui;

import com.colorpl.show.dto.CreateByApiIdRequest;
import com.colorpl.show.dto.CreateByDateRequest;
import com.colorpl.show.dto.SearchShowDetailCondition;
import com.colorpl.show.dto.ShowDetailListResponse;
import com.colorpl.show.dto.ShowDetailResponse;
import com.colorpl.show.service.CreateShowService;
import com.colorpl.show.service.SearchShowDetailService;
import com.colorpl.show.service.SearchShowScheduleService;
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

@RequestMapping("/shows")
@RequiredArgsConstructor
@RestController
public class ShowController {

    private final SearchShowDetailService searchShowDetailService;
    private final CreateShowService createShowService;
    private final SearchShowScheduleService searchShowScheduleService;

    @GetMapping
    public ResponseEntity<List<ShowDetailListResponse>> search(
        @ModelAttribute SearchShowDetailCondition condition) {
        return ResponseEntity.ok(searchShowDetailService.search(condition));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(searchShowDetailService.getShowDetail(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createByDate(
        @RequestBody CreateByDateRequest request) {

        createShowService.createByDate(request.getFrom(), request.getTo());

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Long> createByApiId(@RequestBody CreateByApiIdRequest request) {

        Long id = createShowService.createByApiId(request.getApiId());

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/shows/{id}")
            .buildAndExpand(id)
            .toUri();

        return ResponseEntity.created(uri).body(id);
    }

    @GetMapping("/schedules")
    public ResponseEntity<?> showSchedules(
        @RequestParam Long showDetailId,
        @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(searchShowScheduleService.search(showDetailId, date));
    }
}
