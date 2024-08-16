package com.colorpl.show.domain;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum SeatClass {
    EMPTY(-1),
    B(0),
    A(1),
    S(2),
    R(3);

    private final Integer numberOfSeatClasses;

    SeatClass(Integer numberOfSeatClasses) {
        this.numberOfSeatClasses = numberOfSeatClasses;
    }

    private static final Map<Integer, SeatClass> integerToSeatClass = Stream.of(values())
        .collect(Collectors.toMap(SeatClass::getNumberOfSeatClasses, e -> e));

    public static Optional<SeatClass> fromInteger(Integer numberOfSeatClasses) {
        return Optional.ofNullable(integerToSeatClass.get(numberOfSeatClasses));
    }
}
