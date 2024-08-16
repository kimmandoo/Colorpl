package com.colorpl.global.common.exception;

public class CategoryLimitException extends BusinessException {
    public CategoryLimitException() {
        super(Messages.CATEGORY_LIMIT);
    }
}