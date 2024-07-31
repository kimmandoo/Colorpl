package com.colorpl.ticket.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FindTicketDetailServiceTest {

    @Autowired
    private CreateUnformattedTicketService createUnformattedTicketService;
    @Autowired
    private FindTicketDetailService findTicketDetailService;

    @Test
    void findTicketDetail() {
        String dateTime = LocalDateTime.now().toString();
        CreateUnformattedTicketRequest request = CreateUnformattedTicketRequest.builder()
            .category("연극")
            .name("test")
            .dateTime(dateTime)
            .theater("test")
            .build();
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test".getBytes());
        Long ticketId = createUnformattedTicketService.create(request, file);
        FindTicketDetailResponse response = findTicketDetailService.findTicketDetail(
            ticketId);
        assertThat(response).isNotNull();
        assertThat(response.getCategory()).isEqualTo("연극");
        assertThat(response.getName()).isEqualTo("test");
        assertThat(response.getDateTime()).isEqualTo(dateTime);
        assertThat(response.getTheater()).isEqualTo("test");
    }
}