package com.colorpl.ticket.application;

import com.colorpl.ticket.domain.FindTicketDetailResponse;
import com.colorpl.ticket.domain.FindTicketDetailService;
import com.colorpl.ticket.domain.TicketRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
class FindTicketDetailServiceTest {

    @Autowired
    CreateTicketService createTicketService;
    @Autowired
    FindTicketDetailService findTicketDetailService;
    @Autowired
    TicketRepository ticketRepository;

    @Test
    void find() {

        CreateTicketRequest request = CreateTicketRequest.builder()
            .name("test")
            .theater("test")
            .dateTime(LocalDateTime.now().toString())
            .seat("seat")
            .category("연극")
            .build();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
            "test".getBytes());

        Long id = createTicketService.create(request, file);
        FindTicketDetailResponse response = findTicketDetailService.find(id);

        System.out.println(response.getFilepath());
    }
}