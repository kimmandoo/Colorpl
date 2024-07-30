package com.colorpl.ticket.ui;

import com.colorpl.ticket.application.FindTicketDetailResponse;
import com.colorpl.ticket.application.FindTicketDetailService;
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
    public ResponseEntity<FindTicketDetailResponse> findTicketDetail(@PathVariable Long id) {
        return ResponseEntity.ok(findTicketDetailService.findTicketDetail(id));
    }
}
