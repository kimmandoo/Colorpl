package com.colorpl.global.common.exception;

public class EmailNotFoundException extends BusinessException {
    public EmailNotFoundException() {
        super(Messages.EMAIL_NOT_FOUND);
    }
}