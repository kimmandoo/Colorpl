package com.colorpl.show.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetSchedulesResponse {

    private String name;
    @Builder.Default
    private List<Hall> hall = new ArrayList<>();

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class Hall {

        private String name;
        private Integer countSeats;
        @Builder.Default
        private List<Timetable> timetable = new ArrayList<>();

        @Getter
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        @Builder
        public static class Timetable {

            private Long showScheduleId;
            private LocalDateTime startTime;
            private LocalDateTime endTime;
            private Integer remainingSeats;
        }
    }
}
