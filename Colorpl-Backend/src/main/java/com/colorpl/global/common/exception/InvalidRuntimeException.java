package com.colorpl.global.common.exception;

public class InvalidRuntimeException extends BusinessException {

    public InvalidRuntimeException(String runtime) {
        super(Messages.INVALID_RUNTIME.formatted(runtime));
    }
}
