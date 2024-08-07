package com.colorpl.ticket.ui;

import com.colorpl.ticket.application.CreateTicketRequest;
import com.colorpl.ticket.application.CreateTicketResponse;
import com.colorpl.ticket.application.CreateTicketService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
public class CreateTicketController {

    private final CreateTicketService createTicketService;

    @PostMapping("tickets")
    public ResponseEntity<CreateTicketResponse> create(
        @RequestPart CreateTicketRequest request,
        @RequestPart(required = false) MultipartFile file
    ) {

        Long id = createTicketService.create(request, file);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/tickets/{id}")
            .buildAndExpand(id)
            .toUri();

        CreateTicketResponse response = CreateTicketResponse.builder()
            .ticketId(id)
            .build();

        return ResponseEntity.created(uri).body(response);
    }
}
