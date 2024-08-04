package com.colorpl.ticket.query;

import lombok.Getter;

@Getter
public class MonthlyTicketListRequest {

    private Integer memberId;
    private Period period;
}
