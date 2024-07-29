package com.colorpl.show.domain.detail;

import static org.assertj.core.api.Assertions.assertThat;

import com.colorpl.show.application.RetrieveShowDetailApiService;
import com.colorpl.show.application.ShowDetailApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CreateShowDetailServiceTest {

    @Autowired
    private CreateShowDetailService createShowDetailService;
    @Autowired
    private RetrieveShowDetailApiService retrieveShowDetailApiService;

    @Test
    void create() {
        ShowDetailApiResponse showDetailApiResponse = retrieveShowDetailApiService.retrieve(
            "PF233138");
        ShowDetail showDetail = createShowDetailService.create(showDetailApiResponse.getItem());
        assertThat(showDetail.getPriceBySeatClass()).containsEntry("R석", 77000);
        assertThat(showDetail.getPriceBySeatClass()).containsEntry("S석", 66000);
        assertThat(showDetail.getPriceBySeatClass()).containsEntry("A석", 55000);
        assertThat(showDetail.getState()).isEqualTo(ShowState.COMPLETED);
    }
}