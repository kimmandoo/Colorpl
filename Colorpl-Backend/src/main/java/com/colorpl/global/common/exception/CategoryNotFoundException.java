package com.colorpl.global.common.exception;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException(String category) {
        super(Messages.CATEGORY_NOT_FOUND.formatted(category));
    }
}
