package com.colorpl.global.common.exception;

public class InvalidDayOfWeekException extends BusinessException {

    public InvalidDayOfWeekException(String dayOfWeek) {
        super(Messages.INVALID_DAY_OF_WEEK.formatted(dayOfWeek));
    }
}
