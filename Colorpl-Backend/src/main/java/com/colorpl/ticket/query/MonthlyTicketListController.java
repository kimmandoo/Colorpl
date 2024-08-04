package com.colorpl.ticket.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MonthlyTicketListController {

    private final MonthlyTicketListService monthlyTicketListService;

    @GetMapping("/tickets")
    public List<MonthlyTicketListResponse> monthlyTicketList(MonthlyTicketListRequest request) {
        return monthlyTicketListService.monthlyTicketList(request);
    }
}
