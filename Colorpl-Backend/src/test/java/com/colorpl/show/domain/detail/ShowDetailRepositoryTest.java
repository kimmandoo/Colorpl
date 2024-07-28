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
class ShowDetailRepositoryTest {

    @Autowired
    private CreateShowDetailService createShowDetailService;
    @Autowired
    private RetrieveShowDetailApiService retrieveShowDetailApiService;
    @Autowired
    private ShowDetailRepository showDetailRepository;

    @Test
    void findByApiId() {
        ShowDetailApiResponse showDetailApiResponse = retrieveShowDetailApiService.retrieve(
            "PF233138");
        ShowDetail createShowDetail = createShowDetailService.create(
            showDetailApiResponse.getItem());
        ShowDetail findShowDetail = showDetailRepository.findByApiId("PF233138").orElseThrow();
        assertThat(createShowDetail).isEqualTo(findShowDetail);
    }
}