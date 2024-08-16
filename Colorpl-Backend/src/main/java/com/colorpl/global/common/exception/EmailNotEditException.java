package com.colorpl.global.common.exception;

public class EmailNotEditException extends BusinessException {
    public EmailNotEditException() {
        super(Messages.EMAIL_NOT_EDIT);
    }
}