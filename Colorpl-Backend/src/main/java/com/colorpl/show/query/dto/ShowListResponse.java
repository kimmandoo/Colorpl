package com.colorpl.show.query.dto;

import com.colorpl.show.domain.detail.Category;
import com.colorpl.show.domain.detail.ShowDetail;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShowListResponse {

    private Long id;
    private String name;
    private String runtime;
    private Map<String, Integer> priceBySeatClass;
    private String posterImagePath;
    private Category category;

    public static ShowListResponse from(ShowDetail showDetail) {
        return ShowListResponse.builder()
            .id(showDetail.getId())
            .name(showDetail.getName())
            .runtime(showDetail.getRuntime())
            .priceBySeatClass(showDetail.getPriceBySeatClass())
            .posterImagePath(showDetail.getPosterImagePath())
            .category(showDetail.getCategory())
            .build();
    }
}
