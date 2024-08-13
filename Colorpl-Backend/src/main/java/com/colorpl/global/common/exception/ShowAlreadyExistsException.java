package com.colorpl.global.common.exception;

public class ShowAlreadyExistsException extends BusinessException {

    public ShowAlreadyExistsException(String apiId) {
        super(Messages.SHOW_ALREADY_EXISTS.formatted(apiId));
    }
}
