package com.colorpl.reservation.status.domain;

import com.colorpl.global.common.exception.ReservationStatusAlreadyDisabledException;
import com.colorpl.global.common.exception.ReservationStatusAlreadyEnabledException;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.show.domain.Seat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@RedisHash("ReservationStatus")
public class ReservationStatus {

    @Id
    private Long showScheduleId;

    @TimeToLive
    private Long expiration;

    @Builder.Default
    private Map<String, Boolean> reserved = new HashMap<>();

    public static ReservationStatus createReservationStatus(
        Long showScheduleId,
        Long expiration,
        Integer rows,
        Integer cols,
        List<ReservationDetail> reservationDetails
    ) {
        ReservationStatus reservationStatus = ReservationStatus.builder()
            .showScheduleId(showScheduleId)
            .expiration(expiration)
            .build();
        reservationStatus.createReserved(rows, cols, reservationDetails);
        return reservationStatus;
    }

    public void updateExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public void enableReservation(Integer row, Integer col, Long expiration) {
        Seat seat = Seat.builder()
            .row(row)
            .col(col)
            .build();
        if (reserved.get(seat.toString())) {
            throw new ReservationStatusAlreadyEnabledException(showScheduleId, row, col);
        }
        reserved.put(seat.toString(), true);
        updateExpiration(expiration);
    }

    public void disableReservation(Integer row, Integer col, Long expiration) {
        Seat seat = Seat.builder()
            .row(row)
            .col(col)
            .build();
        if (!reserved.get(seat.toString())) {
            throw new ReservationStatusAlreadyDisabledException(showScheduleId, row, col);
        }
        reserved.put(seat.toString(), false);
        updateExpiration(expiration);
    }

    private void createReserved(
        Integer rows,
        Integer cols,
        List<ReservationDetail> reservationDetails
    ) {
        createAvailableSeats(rows, cols);
        createUnavailableSeats(reservationDetails);
    }

    private void createAvailableSeats(Integer rows, Integer cols) {
        IntStream.rangeClosed(1, rows)
            .forEach(i -> IntStream.rangeClosed(1, cols)
                .forEach(j -> reserved.putIfAbsent(
                    Seat.builder()
                        .row(i)
                        .col(j)
                        .build()
                        .toString(),
                    true
                )));
    }

    private void createUnavailableSeats(List<ReservationDetail> reservationDetails) {
        reservationDetails
            .forEach(reservationDetail -> reserved.put(
                Seat.builder()
                    .row(Integer.valueOf(reservationDetail.getRow()))
                    .col(Integer.valueOf(reservationDetail.getCol()))
                    .build()
                    .toString(),
                false
            ));
    }
}
