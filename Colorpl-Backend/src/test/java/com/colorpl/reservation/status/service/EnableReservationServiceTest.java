package com.colorpl.reservation.status.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.colorpl.global.common.exception.ReservationStatusAlreadyEnabledException;
import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import com.colorpl.show.domain.Seat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EnableReservationServiceTest {

    @Autowired
    private CreateReservationStatusService createReservationStatusService;
    @Autowired
    private ReservationStatusRepository reservationStatusRepository;
    @Autowired
    private EnableReservationService enableReservationService;
    private Seat seat;

    @BeforeEach
    void setUp() {
        createReservationStatusService.createReservationStatus(2L);
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
    void enableReservation() {
        disableReservation();
        enableReservationService.enableReservation(2L, 0, 0);
        ReservationStatus found = reservationStatusRepository.findById(2L).orElseThrow();
        assertThat(found.getReserved().get(seat.toString())).isEqualTo(true);
    }

    @Test
    void enableReservation_assertThatThrownBy() {
        assertThatThrownBy(() -> enableReservationService.enableReservation(2L, 0, 0)).isInstanceOf(
            ReservationStatusAlreadyEnabledException.class);
    }

    private void disableReservation() {
        ReservationStatus status = reservationStatusRepository.findById(2L).orElseThrow();
        status.getReserved().put(seat.toString(), false);
        reservationStatusRepository.save(status);
    }
}