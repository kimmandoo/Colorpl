package com.colorpl.global.common.exception;

public class ShowAlreadyExistsException extends BusinessException {

    public ShowAlreadyExistsException() {
        super(Messages.SHOW_ALREADY_EXISTS);
    }
}
