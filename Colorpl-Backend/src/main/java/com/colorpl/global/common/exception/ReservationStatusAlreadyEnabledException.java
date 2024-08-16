package com.colorpl.global.common.exception;

public class ReservationStatusAlreadyEnabledException extends BusinessException {

    public ReservationStatusAlreadyEnabledException(Long showScheduleId, Integer row, Integer col) {
        super(Messages.RESERVATION_STATUS_ALREADY_ENABLED.formatted(showScheduleId, row, col));
    }
}
