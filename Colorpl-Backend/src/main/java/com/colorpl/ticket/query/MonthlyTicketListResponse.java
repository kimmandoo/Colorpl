package com.colorpl.ticket.query;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public class MonthlyTicketListResponse {

    private Long ticketId;
    private String name;
    private LocalDateTime dateTime;

    @QueryProjection
    public MonthlyTicketListResponse(Long ticketId, String name, LocalDateTime dateTime) {
        this.ticketId = ticketId;
        this.name = name;
        this.dateTime = dateTime;
    }
}
