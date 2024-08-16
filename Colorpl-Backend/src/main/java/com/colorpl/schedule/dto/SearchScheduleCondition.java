package com.colorpl.schedule.dto;

import com.colorpl.member.Member;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SearchScheduleCondition {

    private Member member;
    private LocalDateTime from;
    private LocalDateTime to;
}
