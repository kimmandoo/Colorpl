package com.colorpl.global.common.exception;

public class ReservationNotFoundException extends BusinessException {
    public ReservationNotFoundException() {
        super(Messages.RESERVATION_NOT_FOUND);
    }
}