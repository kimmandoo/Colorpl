package com.colorpl.show.domain.detail;

public enum ShowState {
    SCHEDULED, SHOWING, COMPLETED;

    public static ShowState from(String string) {
        return switch (string) {
            case "공연예정" -> SCHEDULED;
            case "공연중" -> SHOWING;
            case "공연완료" -> COMPLETED;
            default -> throw new IllegalArgumentException("Unknown ShowState: " + string);
        };
    }
}
