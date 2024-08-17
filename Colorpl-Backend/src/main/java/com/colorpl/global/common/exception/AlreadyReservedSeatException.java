package com.colorpl.global.common.exception;

public class AlreadyReservedSeatException extends BusinessException {

    public AlreadyReservedSeatException() {
        super(Messages.RESERVATION_STATUS_ALREADY_DISABLED);
    }
}
