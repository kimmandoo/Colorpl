package com.colorpl.reservation.status.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import com.colorpl.show.domain.Seat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GetReservationStatusServiceTest {

    @Autowired
    private CreateReservationStatusService createReservationStatusService;
    @Autowired
    private ReservationStatusRepository reservationStatusRepository;
    @Autowired
    private GetReservationStatusService getReservationStatusService;
    private Seat seat;

    @BeforeEach
    void setUp() {
        createReservationStatusService.createReservationStatus(0L);
        seat = Seat.builder()
            .row(0)
            .col(0)
            .build();
    }

    @AfterEach
    void tearDown() {
        reservationStatusRepository.deleteAll();
    }

    @Test
    void getReservationStatus() {
        ReservationStatus reservationStatus = getReservationStatusService.getReservationStatus(0L);
        assertThat(reservationStatus.getReserved().get(seat.toString())).isEqualTo(true);
    }
}