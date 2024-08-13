package com.colorpl.global.common.exception;

public class ScheduleNotFoundException extends BusinessException {

    public ScheduleNotFoundException(Long id) {
        super(Messages.SCHEDULE_NOT_FOUND.formatted(id));
    }
}
