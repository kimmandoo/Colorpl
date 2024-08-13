package com.colorpl.global.common.exception;

public class ShowStateNotFoundException extends BusinessException {

    public ShowStateNotFoundException(String state) {
        super(Messages.SHOW_STATE_NOT_FOUND.formatted(state));
    }
}
