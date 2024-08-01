package com.colorpl.global.common.exception;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super(Messages.EMAIL_ALREADY_EXISTS);
    }
}
