package com.colorpl.show.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Area {
    SEOUL("서울특별시"),
    BUSAN("부산광역시"),
    DAEGU("대구광역시"),
    INCHEON("인천광역시"),
    GWANGJU("광주광역시"),
    DAEJEON("대전광역시"),
    ULSAN("울산광역시"),
    SEJON("세종특별자치시"),
    GYEONGGI("경기도"),
    GANGWON("강원특별자치도"),
    CHUNGCHEONGBUKDO("충청북도"),
    CHUNGCHEONGNAMDO("충청남도"),
    JEOLLABUKDO("전라북도"),
    JEOLLANAMDO("전라남도"),
    GYEONGSANGBUKDO("경상북도"),
    GYEONGSANGNAMDO("경상남도"),
    JEJU("제주특별자치도"),
    ETC("기타");

    private final String name;

    Area(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    private static final Map<String, Area> stringToEnum = new HashMap<>();

    static {
        for (Area e : values()) {
            stringToEnum.put(e.toString(), e);
        }
        stringToEnum.put("강원도", GANGWON);
        stringToEnum.put("해외", ETC);
    }

    public static Optional<Area> fromString(String name) {
        return Optional.ofNullable(stringToEnum.get(name));
    }
}
