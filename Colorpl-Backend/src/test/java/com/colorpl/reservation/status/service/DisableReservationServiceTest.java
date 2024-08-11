package com.colorpl.reservation.status.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.colorpl.global.common.exception.ReservationStatusAlreadyDisabledException;
import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import com.colorpl.show.domain.Seat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DisableReservationServiceTest {

    @Autowired
    private CreateReservationStatusService createReservationStatusService;
    @Autowired
    private ReservationStatusRepository reservationStatusRepository;
    @Autowired
    private DisableReservationService disableReservationService;
    private Seat seat;

    @BeforeEach
    void setUp() {
        createReservationStatusService.createReservationStatus(2L);
        seat = Seat.builder()
            .row(1)
            .col(1)
            .build();
    }

    @AfterEach
    void tearDown() {
        reservationStatusRepository.deleteAll();
    }

    @Test
    void disableReservation() {
        disableReservationService.disableReservation(2L, 1, 1);
        ReservationStatus found = reservationStatusRepository.findById(2L).orElseThrow();
        assertThat(found.getReserved().get(seat.toString())).isEqualTo(false);
    }

    @Test
    void disableReservation_assertThatThrownBy() {
        assertThatThrownBy(() -> {
            disableReservationService.disableReservation(2L, 1, 1);
            disableReservationService.disableReservation(2L, 1, 1);
        }).isInstanceOf(ReservationStatusAlreadyDisabledException.class);
    }
}