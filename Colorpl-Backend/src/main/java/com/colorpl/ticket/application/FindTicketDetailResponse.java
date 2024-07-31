package com.colorpl.ticket.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class FindTicketDetailResponse {

    private String category;
    private String name;
    private String dateTime;
    private String theater;
    private String filepath;
}
