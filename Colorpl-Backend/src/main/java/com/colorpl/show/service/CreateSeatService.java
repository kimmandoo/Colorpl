package com.colorpl.show.service;

import static com.colorpl.show.service.SeatRatio.SEAT_RATIO;

import com.colorpl.global.common.exception.InvalidSeatClassException;
import com.colorpl.show.domain.Seat;
import com.colorpl.show.domain.SeatClass;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.repository.SeatRepository;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSeatService {

    private final SeatRepository seatRepository;
    @Value("${seat.rows}")
    private int rows;
    @Value("${seat.cols}")
    private int cols;

    @Transactional
    public void createSeat(ShowDetail showDetail) {
        double[] seatRatio = SEAT_RATIO[showDetail.getPriceBySeatClass().size()];
        IntStream.range(0, rows)
            .forEach(i -> {
                int index = Arrays.binarySearch(seatRatio, (double) i / rows * cols);
                int seatClass = getSeatClass(index, showDetail);
                IntStream.range(0, cols)
                    .forEach(j -> Seat.builder()
                        .showDetail(showDetail)
                        .row(i)
                        .col(j)
                        .seatClass(SeatClass.fromInteger(seatClass)
                            .orElseThrow(() -> new InvalidSeatClassException(seatClass)))
                        .build());
            });
        seatRepository.batchInsert(showDetail.getSeats());
    }

    private int getSeatClass(int index, ShowDetail showDetail) {
        if (index < 0) {
            index = -index - 1;
        }
        if (index == showDetail.getPriceBySeatClass().size()) {
            index -= 1;
        }
        return index;
    }
}
