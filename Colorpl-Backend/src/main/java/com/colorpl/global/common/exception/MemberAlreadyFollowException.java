package com.colorpl.global.common.exception;

public class MemberAlreadyFollowException extends BusinessException {
    public MemberAlreadyFollowException() {
        super(Messages.MEMBER_ALREADY_FOLLOW);
    }
}