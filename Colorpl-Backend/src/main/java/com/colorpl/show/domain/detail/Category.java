package com.colorpl.show.domain.detail;

public enum Category {
    PLAY, DANCE, PUBLIC_DANCE, WESTERN_MUSIC, KOREAN_MUSIC, POPULAR_MUSIC, COMPLEX, CIRCUS_MAGIC, MUSICAL;

    public static Category fromString(String category) {
        return switch (category) {
            case "연극" -> PLAY;
            case "무용(서양/한국무용)" -> DANCE;
            case "대중무용" -> PUBLIC_DANCE;
            case "서양음악(클래식)" -> WESTERN_MUSIC;
            case "한국음악(국악)" -> KOREAN_MUSIC;
            case "대중음악" -> POPULAR_MUSIC;
            case "복합" -> COMPLEX;
            case "서커스/마술" -> CIRCUS_MAGIC;
            case "뮤지컬" -> MUSICAL;
            default -> throw new IllegalArgumentException(category);
        };
    }
}
