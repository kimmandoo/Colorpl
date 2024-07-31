package com.colorpl.ticket.ui;

import com.colorpl.ticket.application.CreateUnformattedTicketRequest;
import com.colorpl.ticket.application.CreateUnformattedTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class CreateUnformattedTicketController {

    private final CreateUnformattedTicketService createUnformattedTicketService;

    @PostMapping("tickets/unformatted")
    public ResponseEntity<?> create(
        @RequestPart CreateUnformattedTicketRequest request,
        @RequestPart(required = false) MultipartFile file) {
        Long ticketId = createUnformattedTicketService.create(request, file).getId();
        return ResponseEntity.ok(ticketId);
    }
}
