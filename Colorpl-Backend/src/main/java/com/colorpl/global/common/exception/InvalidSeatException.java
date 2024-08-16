package com.colorpl.global.common.exception;

public class InvalidSeatException extends BusinessException {

    public InvalidSeatException(Integer row, Integer col) {
        super(Messages.INVALID_SEAT.formatted(row, col));
    }
}
