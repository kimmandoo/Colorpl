package com.colorpl.ticket.ui;

import com.colorpl.ticket.domain.FindTicketDetailResponse;
import com.colorpl.ticket.domain.FindTicketDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FindTicketDetailController {

    private final FindTicketDetailService findTicketDetailService;

    @GetMapping("tickets/{id}")
    public ResponseEntity<FindTicketDetailResponse> find(@PathVariable Long id) {
        return ResponseEntity.ok(findTicketDetailService.find(id));
    }
}
