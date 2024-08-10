package com.colorpl.reservation.status.domain;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash("ReservationStatus")
public class ReservationStatus {

    @Id
    private Long showScheduleId;

    @Builder.Default
    private Map<String, Boolean> reserved = new HashMap<>();
}
