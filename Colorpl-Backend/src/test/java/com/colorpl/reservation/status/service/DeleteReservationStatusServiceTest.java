package com.colorpl.reservation.status.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeleteReservationStatusServiceTest {

    @Autowired
    private CreateReservationStatusService createReservationStatusService;
    @Autowired
    private ReservationStatusRepository reservationStatusRepository;
    @Autowired
    private DeleteReservationStatusService deleteReservationStatusService;

    @BeforeEach
    void setUp() {
        createReservationStatusService.createReservationStatus(0L);
    }

    @AfterEach
    void tearDown() {
        reservationStatusRepository.deleteAll();
    }

    @Test
    void deleteReservationStatus() {
        deleteReservationStatusService.deleteReservationStatus(0L);
        assertThat(reservationStatusRepository.count()).isEqualTo(0L);
    }
}