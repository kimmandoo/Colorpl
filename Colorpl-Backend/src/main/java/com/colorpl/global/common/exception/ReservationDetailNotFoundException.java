package com.colorpl.global.common.exception;

public class ReservationDetailNotFoundException extends BusinessException {
    public ReservationDetailNotFoundException() {
        super(Messages.RESERVATION_DETAIL_NOT_FOUND);
    }
}