package com.colorpl.global.common.exception;

public class InvalidSeatClassException extends BusinessException {

    public InvalidSeatClassException(Integer numberOfSeatClasses) {
        super(Messages.INVALID_SEAT_CLASS.formatted(numberOfSeatClasses));
    }
}
