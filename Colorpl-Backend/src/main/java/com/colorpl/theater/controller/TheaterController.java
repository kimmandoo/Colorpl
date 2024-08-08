package com.colorpl.theater.controller;

import com.colorpl.theater.service.CreateTheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/theater")
@RequiredArgsConstructor
public class TheaterController {

    private final CreateTheaterService createTheaterService;

    @PostMapping
    public ResponseEntity<Integer> createTheater(@RequestBody String apiId) {
        return ResponseEntity.ok(createTheaterService.createTheater(apiId));
    }
}
