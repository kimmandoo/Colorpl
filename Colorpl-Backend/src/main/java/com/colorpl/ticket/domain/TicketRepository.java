package com.colorpl.ticket.domain;

import com.colorpl.ticket.query.TicketRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

}
