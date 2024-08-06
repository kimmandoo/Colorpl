package com.colorpl.show.ui;

import com.colorpl.show.application.CreateShowService;
import com.colorpl.show.query.application.ShowDetailListService;
import com.colorpl.show.query.dao.ShowDetailSearchCondition;
import com.colorpl.show.query.dto.ShowDetailListResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("/shows")
@RequiredArgsConstructor
@RestController
public class ShowController {

    private final CreateShowService createShowService;
    private final ShowDetailListService showDetailListService;

    @GetMapping("/")
    public ResponseEntity<List<ShowDetailListResponse>> getShowDetailList(
        @ModelAttribute ShowDetailSearchCondition condition) {

        return ResponseEntity.ok(showDetailListService.showDetailList(condition));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createByDate(
        @RequestBody CreateByDateRequest request) {

        createShowService.createByDate(request.getFrom(), request.getTo());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/")
    public ResponseEntity<Long> createByApiId(@RequestBody CreateByApiIdRequest request) {

        Long id = createShowService.createByApiId(request.getApiId());

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/shows/{id}")
            .buildAndExpand(id)
            .toUri();

        return ResponseEntity.created(uri).body(id);
    }
}
