package com.colorpl.ticket.application;

import com.colorpl.show.domain.detail.Category;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateUnformattedTicketService {

    private final TicketRepository ticketRepository;

    public Ticket create(
        @RequestPart CreateUnformattedTicketRequest request,
        @RequestPart(required = false) MultipartFile attachFile) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Ticket ticket = Ticket.builder()
            .category(Category.fromString(request.getCategory()))
            .name(request.getName())
            .dateTime(LocalDateTime.parse(request.getDateTime(), formatter))
            .theater(request.getTheater())
            .build();
        return ticketRepository.save(ticket);
    }
}
