package com.colorpl.show.dto;

import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.ShowDetail;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SearchShowsResponse {

    private Integer id;
    private String name;
    private String runtime;
    private Map<String, Integer> priceBySeatClass;
    private String posterImagePath;
    private Category category;

    public static SearchShowsResponse from(ShowDetail showDetail) {
        return SearchShowsResponse.builder()
            .id(showDetail.getId())
            .name(showDetail.getName())
            .runtime(showDetail.getRuntime())
            .priceBySeatClass(showDetail.getPriceBySeatClass())
            .posterImagePath(showDetail.getPosterImagePath())
            .category(showDetail.getCategory())
            .build();
    }
}
