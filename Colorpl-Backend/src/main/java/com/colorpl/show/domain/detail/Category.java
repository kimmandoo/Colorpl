package com.colorpl.show.domain.detail;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Category {
    MOVIE("영화"),
    PLAY("연극"),
    CONCERT("콘서트"),
    CIRCUS("서커스"),
    EXHIBITION("전시회");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    private static final Map<String, Category> stringToEnum = new HashMap<>();

    static {
        for (Category e : values()) {
            stringToEnum.put(e.toString(), e);
        }
        stringToEnum.put("무용(서양/한국무용)", PLAY);
        stringToEnum.put("대중무용", CONCERT);
        stringToEnum.put("서양음악(클래식)", CONCERT);
        stringToEnum.put("한국음악(국악)", CONCERT);
        stringToEnum.put("대중음악", CONCERT);
        stringToEnum.put("복합", CIRCUS);
        stringToEnum.put("서커스/마술", CIRCUS);
        stringToEnum.put("뮤지컬", PLAY);
    }

    public static Optional<Category> fromString(String name) {
        return Optional.ofNullable(stringToEnum.get(name));
    }
}
