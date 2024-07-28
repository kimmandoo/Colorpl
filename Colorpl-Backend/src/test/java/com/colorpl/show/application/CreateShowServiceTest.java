package com.colorpl.show.application;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CreateShowServiceTest {

    @Autowired
    private CreateShowService createShowService;

    @Test
    void create() {
        createShowService.create(LocalDate.now(), LocalDate.now().plusDays(1));
    }
}