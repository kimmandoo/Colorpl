package com.colorpl.show.dto;

import com.colorpl.show.domain.ShowDetail;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchShowScheduleCondition {

    private ShowDetail showDetail;
    private LocalDateTime from;
    private LocalDateTime to;
}
