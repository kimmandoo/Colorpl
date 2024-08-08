package com.colorpl.global.common.exception;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException(String category) {
        super(category);
    }
}
