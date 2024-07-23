package com.colorpl.show.application;

import com.colorpl.show.domain.Show;
import com.colorpl.show.domain.ShowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CreateShowServiceTest {

    @Autowired
    CreateShowService createShowService;
    @Autowired
    ShowRepository showRepository;

    @Test
    void createShow() {

        Integer id = createShowService.createShow("PF132236");
        Show show = showRepository.findById(id).orElseThrow();

        assertThat(show.getName()).isEqualTo("우리 연애할까?");
        assertThat(show.getPriceBySeatClass()).containsEntry("전석", 30000);
    }

}
