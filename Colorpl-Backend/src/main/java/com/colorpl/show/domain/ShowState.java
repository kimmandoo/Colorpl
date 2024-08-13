package com.colorpl.show.domain;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ShowState {
    SCHEDULED("공연예정"),
    SHOWING("공연중"),
    COMPLETED("공연완료");

    private final String name;

    ShowState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    private static final Map<String, ShowState> stringToEnum = Stream.of(values())
        .collect(Collectors.toMap(Objects::toString, e -> e));

    public static Optional<ShowState> fromString(String name) {
        return Optional.ofNullable(stringToEnum.get(name));
    }
}
