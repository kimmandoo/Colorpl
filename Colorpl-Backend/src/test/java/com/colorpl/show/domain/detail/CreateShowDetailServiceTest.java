package com.colorpl.show.domain.detail;

import com.colorpl.show.application.RetrieveShowDetailApiService;
import com.colorpl.show.infra.ShowDetailApiResponse;
import org.assertj.core.api.Assertions;
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

        Assertions.assertThat(showDetail.getPriceBySeatClass()).containsEntry("R석", 77000);
        Assertions.assertThat(showDetail.getPriceBySeatClass()).containsEntry("S석", 66000);
        Assertions.assertThat(showDetail.getPriceBySeatClass()).containsEntry("A석", 55000);
        Assertions.assertThat(showDetail.getState()).isEqualTo(ShowState.COMPLETED);
    }
}