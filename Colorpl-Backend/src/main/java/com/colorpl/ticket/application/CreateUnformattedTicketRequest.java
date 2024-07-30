package com.colorpl.ticket.application;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUnformattedTicketRequest {

    private String category;
    private String name;
    private String dateTime;
    private String theater;
}
