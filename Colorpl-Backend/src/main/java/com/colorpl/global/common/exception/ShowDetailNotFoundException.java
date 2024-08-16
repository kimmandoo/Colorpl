package com.colorpl.global.common.exception;

public class ShowDetailNotFoundException extends BusinessException {

    public ShowDetailNotFoundException() {
        super(Messages.SHOW_DETAIL_NOT_FOUND);
    }
}
