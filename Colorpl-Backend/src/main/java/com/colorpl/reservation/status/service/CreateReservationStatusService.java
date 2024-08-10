package com.colorpl.reservation.status.service;

import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import com.colorpl.show.domain.Seat;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReservationStatusService {

    @Value("${seat.rows}")
    private int rows;

    @Value("${seat.cols}")
    private int cols;

    @Value("${time.to.live}")
    private Long expiration;

    private final ReservationStatusRepository reservationStatusRepository;

    public void createReservationStatus(Long showScheduleId) {
        reservationStatusRepository.findById(showScheduleId).orElseGet(() -> {
            ReservationStatus reservationStatus = ReservationStatus.builder()
                .showScheduleId(showScheduleId)
                .expiration(expiration)
                .build();
            createReserved(reservationStatus);
            return reservationStatusRepository.save(reservationStatus);
        });
    }

    private void createReserved(ReservationStatus reservationStatus) {
        IntStream.rangeClosed(0, rows)
            .forEach(i -> IntStream.rangeClosed(0, cols)
                .forEach(j -> {
                    Seat seat = Seat.builder()
                        .row(i)
                        .col(j)
                        .build();
                    reservationStatus.getReserved().put(seat.toString(), true);
                }));
    }
}
