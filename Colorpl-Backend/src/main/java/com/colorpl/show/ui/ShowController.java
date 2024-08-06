package com.colorpl.show.ui;

import com.colorpl.show.application.CreateShowService;
import com.colorpl.show.query.application.ShowListService;
import com.colorpl.show.query.dto.ShowListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/shows")
@RequiredArgsConstructor
@RestController
public class ShowController {

    private final CreateShowService createShowService;
    private final ShowListService showListService;

    @GetMapping("/")
    public ResponseEntity<List<ShowListResponse>> test() {
        return ResponseEntity.ok(showListService.showList());
    }

    @PostMapping("/")
    public ResponseEntity<Long> createByShowApiId(String showApiId) {
        Long showDetailId = createShowService.createByShowApiId(showApiId);
        return ResponseEntity.status(HttpStatus.CREATED).body(showDetailId);
    }
}
