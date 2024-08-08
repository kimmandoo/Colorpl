package com.colorpl.global.common.exception;

public class TheaterAlreadyExistsException extends BusinessException {

    public TheaterAlreadyExistsException(String apiId) {
        super(Messages.THEATER_ALREADY_EXISTS.concat(apiId));
    }
}
