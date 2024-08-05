package com.colorpl.global.common.exception;

public class ReviewNotFoundException extends BusinessException {
    public ReviewNotFoundException() {
        super(Messages.REVIEW_NOT_FOUND);
    }
}