package com.colorpl.global.common.exception;

import static com.colorpl.global.common.exception.Messages.MEMBER_REQUEST_NOT_MATCH;

public class MemberRequestNotMatchException extends BusinessException{


    public MemberRequestNotMatchException() {
        super(MEMBER_REQUEST_NOT_MATCH);
    }
}
