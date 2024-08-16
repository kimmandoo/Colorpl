package com.colorpl.reservation.status.domain;

import com.colorpl.global.common.exception.InvalidSeatException;
import com.colorpl.global.common.exception.ReservationStatusAlreadyDisabledException;
import com.colorpl.global.common.exception.ReservationStatusAlreadyEnabledException;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.show.domain.SeatClass;
import com.colorpl.show.domain.ShowDetail;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
    private Map<String, Item> reserved = new HashMap<>();

    public static ReservationStatus createReservationStatus(
        Long showScheduleId,
        Long expiration,
        Integer rows,
        Integer cols,
        ShowDetail showDetail,
        List<ReservationDetail> reservationDetails
    ) {
        ReservationStatus reservationStatus = ReservationStatus.builder()
            .showScheduleId(showScheduleId)
            .expiration(expiration)
            .build();
        reservationStatus.createReserved(rows, cols, showDetail, reservationDetails);
        return reservationStatus;
    }

    public static String getKey(Integer row, Integer col) {
        return "%d:%d".formatted(row, col);
    }

    public void updateExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public void enableReservation(Integer row, Integer col, Long expiration) {
        String key = getKey(row, col);
        if (!reserved.containsKey(key)) {
            throw new InvalidSeatException(row, col);
        }
        Item findItem = reserved.get(key);
        if (!findItem.getIsReserved()) {
            throw new ReservationStatusAlreadyEnabledException(showScheduleId, row, col);
        }
        Item createItem = Item.builder()
            .row(findItem.row)
            .col(findItem.col)
            .name(findItem.getName())
            .grade(findItem.getGrade())
            .isReserved(false)
            .build();
        reserved.put(key, createItem);
        updateExpiration(expiration);
    }

    public void disableReservation(Integer row, Integer col, Long expiration) {
        String key = getKey(row, col);
        if (!reserved.containsKey(key)) {
            throw new InvalidSeatException(row, col);
        }
        Item findItem = reserved.get(key);
        if (findItem.getIsReserved()) {
            throw new ReservationStatusAlreadyDisabledException(showScheduleId, row, col);
        }
        Item createItem = Item.builder()
            .row(findItem.row)
            .col(findItem.col)
            .name(findItem.getName())
            .grade(findItem.getGrade())
            .isReserved(true)
            .build();
        reserved.put(key, createItem);
        updateExpiration(expiration);
    }

    private void createReserved(
        Integer rows,
        Integer cols,
        ShowDetail showDetail,
        List<ReservationDetail> reservationDetails
    ) {
        IntStream.range(0, rows)
            .forEach(i -> IntStream.range(0, cols)
                .forEach(j -> reserved.put(
                    getKey(i, j),
                    getItem(showDetail, i, j)
                )));
        reservationDetails.forEach(
            reservationDetail -> disableReservation(Integer.valueOf(reservationDetail.getRow()),
                Integer.valueOf(reservationDetail.getCol()), expiration));
    }

    private Item getItem(ShowDetail showDetail, int i, int j) {
        SeatClass seatClass = showDetail.getSeats().stream()
            .filter(seat -> seat.getRow().equals(i) && seat.getCol().equals(j)).findAny()
            .orElseThrow(() -> new IllegalArgumentException(i + " " + j))
            .getSeatClass();
        return Item.builder()
            .row(i)
            .col(j)
            .name(String.valueOf((char) ('A' + i)).concat(String.valueOf(j + 1)))
            .grade(seatClass)
            .isReserved(false)
            .build();
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class Item {

        private Integer row;
        private Integer col;
        private String name;
        private SeatClass grade;
        private Boolean isReserved;
    }
}
