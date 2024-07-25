package com.colorpl.global.common.exception;

public class ShowScheduleNotFoundException extends BusinessException {
    public ShowScheduleNotFoundException() {
        super(Messages.SHOW_SCHEDULE_NOT_FOUND);
    }
}