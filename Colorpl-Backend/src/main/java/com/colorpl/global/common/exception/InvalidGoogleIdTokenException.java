package com.colorpl.global.common.exception;

public class InvalidGoogleIdTokenException extends BusinessException {
    public InvalidGoogleIdTokenException() {
        super(Messages.INVALID_GOOGLE_ID_TOKEN);
    }
}
