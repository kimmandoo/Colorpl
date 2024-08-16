package com.colorpl.global;

import com.colorpl.global.common.exception.InvalidAreaException;
import com.colorpl.show.domain.Area;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class AreaFormatter implements Formatter<Area> {

    @Override
    public Area parse(String text, Locale locale) throws ParseException {
        return Area.fromString(text).orElseThrow(() -> new InvalidAreaException(text));
    }

    @Override
    public String print(Area object, Locale locale) {
        return object.toString();
    }
}
