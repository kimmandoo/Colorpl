package com.colorpl.show.domain.detail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateSeatService {

    @Value("${seat.rows}")
    private int rows;
    @Value("${seat.cols}")
    private int cols;

    private double[][] weights = {
            {},
            {1.000},
            {.2500, .7500},
            {.1111, .2778, .6111},
            {.0625, .1458, .2708, .5208},
            {.0400, .0900, .1567, .2567, .4567},
            {.0278, .0611, .1028, .1583, .2417, .4083},
            {.0204, .0442, .0728, .1085, .1561, .2276, .3704},
            {.0156, .0335, .0543, .0793, .1106, .1522, .2147, .3397},
            {.0123, .0262, .0421, .0606, .0828, .1106, .1477, .2032, .3143},
            {.0100, .0211, .0336, .0479, .0646, .0846, .1096, .1429, .1929, .2929},
    };

    public void create(ShowDetail showDetail) {
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (index < showDetail.getPriceBySeatClass().size() - 1) {
                    if (i * cols + j > rows * cols * weights[showDetail.getPriceBySeatClass().size()][index]) {
                        index++;
                    }
                }
                Seat seat = Seat.builder()
                        .showDetail(showDetail)
                        .row(i)
                        .col(j)
                        .seatClass(showDetail.getPriceBySeatClass().keySet().stream().toList().get(index))
                        .build();
                showDetail.getSeats().add(seat);
            }
        }
    }
}
