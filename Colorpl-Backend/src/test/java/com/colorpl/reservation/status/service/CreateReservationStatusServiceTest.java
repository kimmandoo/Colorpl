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
class CreateReservationStatusServiceTest {

    @Autowired
    private ReservationStatusRepository reservationStatusRepository;
    @Autowired
    private CreateReservationStatusService createReservationStatusService;
    private Seat seat;

    @BeforeEach
    void setUp() {
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
    void createReservationStatus() {
        createReservationStatusService.createReservationStatus(2L);
        ReservationStatus status = reservationStatusRepository.findById(2L).orElseThrow();
        assertThat(status.getReserved().get(seat.toString())).isEqualTo(true);
    }
}