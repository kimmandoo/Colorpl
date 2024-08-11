package com.colorpl.global.common.exception;

public class ReservationStatusAlreadyDisabledException extends BusinessException {

    public ReservationStatusAlreadyDisabledException(Long showScheduleId, Integer row,
        Integer col) {
        super(Messages.RESERVATION_STATUS_ALREADY_DISABLED.formatted(showScheduleId, row, col));
    }
}
