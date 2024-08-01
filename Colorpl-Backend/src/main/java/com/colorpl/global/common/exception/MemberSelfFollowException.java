package com.colorpl.global.common.exception;

public class MemberSelfFollowException extends BusinessException {
    public MemberSelfFollowException() {
        super(Messages.MEMBER_SELF_FOLLOW_);
    }
}