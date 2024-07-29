package com.colorpl.ticket.application;

import com.colorpl.show.domain.schedule.ShowSchedule;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateFormattedTicketService {

    private final TicketRepository ticketRepository;

    public Ticket create(ShowSchedule showSchedule) {
        Ticket ticket = Ticket.builder()
            .category(showSchedule.getShowDetail().getCategory())
            .name(showSchedule.getShowDetail().getName())
            .dateTime(showSchedule.getDateTime())
            .build();
        return ticketRepository.save(ticket);
    }
}
