package com.colorpl.show.ui;

import com.colorpl.show.domain.detail.ShowDetail;
import com.colorpl.show.domain.detail.ShowDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FindShowDetailController {

    private final ShowDetailRepository showDetailRepository;

    @GetMapping("/shows/{id}")
    public ResponseEntity<ShowDetail> findShowDetail(@PathVariable Long id) {
        return ResponseEntity.ok(showDetailRepository.findById(id).orElseThrow());
    }
}
