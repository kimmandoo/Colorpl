package com.colorpl.show.domain.detail;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Category {
    PLAY("연극"),
    DANCE("무용(서양/한국무용)"),
    PUBLIC_DANCE("대중무용"),
    WESTERN_MUSIC("서양음악(클래식)"),
    KOREAN_MUSIC("한국음악(국악)"),
    POPULAR_MUSIC("대중음악"),
    COMPLEX("복합"),
    CIRCUS_MAGIC("서커스/마술"),
    MUSICAL("뮤지컬"),
    MOVIE("영화"),
    EXHIBITION("전시회");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    private static final Map<String, Category> stringToEnum = Stream.of(values()).collect(
        Collectors.toMap(Category::toString, e -> e));

    public static Optional<Category> fromString(String name) {
        return Optional.ofNullable(stringToEnum.get(name));
    }
}
