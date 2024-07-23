package com.colorpl.show.domain;

public enum ShowState {
    SCHEDULED, PERFORMING, COMPLETED;

    public static ShowState fromString(String string) {
        return switch (string) {
            case "공연예정" -> SCHEDULED;
            case "공연중" -> PERFORMING;
            case "공연완료" -> COMPLETED;
            default -> throw new IllegalArgumentException("Unknown ShowState: " + string);
        };
    }

}
