package com.colorpl.show.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowScheduleListResponse {

    List<TheaterListResponse> theaters;
}
