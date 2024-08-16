package com.colorpl.global.common.exception;

public class MemberMismatchException extends BusinessException {
    public MemberMismatchException() {
        super(Messages.MEMBER_RESERVATION_MISMATCH);
    }
}