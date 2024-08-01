package com.colorpl.ticket.application;

import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@Service
@Transactional
public class FindTicketDetailService {

    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public FindTicketDetailResponse findTicketDetail(Long ticketId) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        return FindTicketDetailResponse.builder()
            .category(ticket.getCategory().toString())
            .name(ticket.getName())
            .dateTime(ticket.getDateTime().toString())
            .theater(ticket.getTheater())
            .filepath(baseUrl + "/images/" + ticket.getFilename())
            .build();
    }
}
