package com.colorpl.ticket.application;

import com.colorpl.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class CreateUnformattedTicketRequest {

    private String category;
    private String name;
    private String dateTime;
    private String theater;
    private Member member;
}
