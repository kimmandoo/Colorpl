package com.colorpl.ticket.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class CreateTicketRequest {

    private String name;
    private String theater;
    private String dateTime;
    private String seat;
    private String category;
}
