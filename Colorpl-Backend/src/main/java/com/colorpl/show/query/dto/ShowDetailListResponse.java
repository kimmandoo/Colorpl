package com.colorpl.show.query.dto;

import com.colorpl.show.domain.detail.Category;
import com.colorpl.show.domain.detail.ShowDetail;
import com.querydsl.core.annotations.QueryProjection;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShowDetailListResponse {

    private Long id;
    private String name;
    private String runtime;
    private Map<String, Integer> priceBySeatClass;
    private String posterImagePath;
    private Category category;

    @QueryProjection
    public ShowDetailListResponse(Long id, String name, String runtime,
        Map<String, Integer> priceBySeatClass, String posterImagePath, Category category) {
        this.id = id;
        this.name = name;
        this.runtime = runtime;
        this.priceBySeatClass = priceBySeatClass;
        this.posterImagePath = posterImagePath;
        this.category = category;
    }

    public static ShowDetailListResponse from(ShowDetail showDetail) {
        return ShowDetailListResponse.builder()
            .id(showDetail.getId())
            .name(showDetail.getName())
            .runtime(showDetail.getRuntime())
            .priceBySeatClass(showDetail.getPriceBySeatClass())
            .posterImagePath(showDetail.getPosterImagePath())
            .category(showDetail.getCategory())
            .build();
    }
}
