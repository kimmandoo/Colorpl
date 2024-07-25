package com.colorpl.global.common.exception;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(Messages.MEMBER_NOT_FOUND);
    }
}