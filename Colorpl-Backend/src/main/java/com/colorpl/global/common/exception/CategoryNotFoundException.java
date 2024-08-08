package com.colorpl.global.common.exception;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException() {
        super(Messages.CATEGORY_NOT_FOUND);
    }
}
