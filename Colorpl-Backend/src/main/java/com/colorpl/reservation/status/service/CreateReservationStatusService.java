package com.colorpl.reservation.status.service;

import com.colorpl.global.common.exception.ShowScheduleNotFoundException;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.reservation.repository.ReservationDetailRepository;
import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.domain.ShowSchedule;
import com.colorpl.show.repository.ShowDetailRepository;
import com.colorpl.show.repository.ShowScheduleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReservationStatusService {

    private final ShowScheduleRepository showScheduleRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final ShowDetailRepository showDetailRepository;

    @Value("${time.to.live}")
    private Long expiration;

    @Value("${seat.rows}")
    private Integer rows;

    @Value("${seat.cols}")
    private Integer cols;

    public ReservationStatus createReservationStatus(Long showScheduleId) {
        ShowSchedule showSchedule = showScheduleRepository
            .findById(showScheduleId)
            .orElseThrow(ShowScheduleNotFoundException::new);
        ShowDetail showDetail = showDetailRepository.getShowDetail(
            showSchedule.getShowDetail().getId());
        List<ReservationDetail> reservationDetails = reservationDetailRepository
            .findByShowSchedule(showSchedule);
        ReservationStatus reservationStatus = ReservationStatus.createReservationStatus(
            showScheduleId,
            expiration,
            rows,
            cols,
            showDetail,
            reservationDetails
        );
        return reservationStatusRepository.save(reservationStatus);
    }
}
