package com.colorpl.global.common.exception;

public class InvalidAreaException extends BusinessException {

    public InvalidAreaException(String area) {
        super(Messages.INVALID_AREA.formatted(area));
    }
}
