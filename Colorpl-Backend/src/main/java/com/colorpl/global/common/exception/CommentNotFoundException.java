package com.colorpl.global.common.exception;

public class CommentNotFoundException extends BusinessException {
    public CommentNotFoundException() {
        super(Messages.COMMENT_NOT_FOUND);
    }
}