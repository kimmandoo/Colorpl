package com.colorpl.reservation.status.repository;

import com.colorpl.reservation.status.domain.ReservationStatus;
import org.springframework.data.repository.CrudRepository;

public interface ReservationStatusRepository extends CrudRepository<ReservationStatus, Long> {

}
