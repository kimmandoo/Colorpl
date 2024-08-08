package com.colorpl.show.dto;

import com.colorpl.show.domain.detail.Category;
import com.colorpl.show.domain.detail.ShowState;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowDetailResponse {

    private Long id;
    private String apiId;
    private String name;
    private String cast;
    private String runtime;
    private Map<String, Integer> priceBySeatClass;
    private String posterImagePath;
    private String area;
    private Category category;
    private ShowState state;
    private List<SeatListResponse> seats;
}
