package com.colorpl.ticket.domain;

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
    public FindTicketDetailResponse find(Long id) {

        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        String filepath = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("images/")
            .path(ticket.getFilename())
            .build()
            .toUriString();

        return FindTicketDetailResponse.builder()
            .filepath(filepath)
            .name(ticket.getName())
            .theater(ticket.getTheater())
            .dateTime(ticket.getDateTime().toString())
            .seat(ticket.getSeat())
            .category(ticket.getCategory().toString())
            .build();
    }
}
