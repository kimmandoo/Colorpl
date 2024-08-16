package com.colorpl.global.common.exception;

public class HallNotFoundException extends BusinessException {

    public HallNotFoundException(String theaterName) {
        super(Messages.HALL_NOT_FOUND.concat(theaterName));
    }
}
