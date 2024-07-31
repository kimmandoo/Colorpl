package com.colorpl.show.ui;

import com.colorpl.show.application.RetrieveShowDetailApiService;
import com.colorpl.show.application.ShowDetailApiResponse;
import com.colorpl.show.domain.detail.CreateShowDetailService;
import com.colorpl.show.domain.detail.ShowDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class FindShowDetailControllerTest {

    @Autowired
    CreateShowDetailService createShowDetailService;
    @Autowired
    FindShowDetailController findShowDetailController;
    @Autowired
    RetrieveShowDetailApiService retrieveShowDetailApiService;

    @Test
    void findShowDetail() {
        ShowDetailApiResponse response = retrieveShowDetailApiService.retrieve("PF233138");
        ShowDetail showDetail = createShowDetailService.create(response.getItem());
        ResponseEntity<ShowDetail> detail = findShowDetailController.findShowDetail(
            showDetail.getId());
    }
}