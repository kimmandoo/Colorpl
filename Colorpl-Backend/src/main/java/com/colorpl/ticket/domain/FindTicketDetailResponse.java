package com.colorpl.ticket.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class FindTicketDetailResponse {

    private String filepath;
    private String name;
    private String theater;
    private String dateTime;
    private String seat;
    private String category;
}
