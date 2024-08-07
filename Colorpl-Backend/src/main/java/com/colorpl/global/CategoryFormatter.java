package com.colorpl.global;

import com.colorpl.global.common.exception.CategoryNotFoundException;
import com.colorpl.show.domain.detail.Category;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class CategoryFormatter implements Formatter<Category> {

    @Override
    public Category parse(String text, Locale locale) throws ParseException {
        return Category.fromString(text).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public String print(Category object, Locale locale) {
        return object.toString();
    }
}
