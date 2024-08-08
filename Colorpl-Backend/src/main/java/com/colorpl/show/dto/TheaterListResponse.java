package com.colorpl.show.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TheaterListResponse {

    private String name;
    private HallListResponse hall;
}
