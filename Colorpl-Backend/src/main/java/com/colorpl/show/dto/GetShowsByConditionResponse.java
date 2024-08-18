package com.colorpl.show.dto;

import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.SeatClass;
import com.colorpl.show.domain.ShowDetail;
import java.io.Serializable;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetShowsByConditionResponse implements Serializable {

    private Integer id;
    private String name;
    private String runtime;
    private Map<SeatClass, Integer> priceBySeatClass;
    private String posterImagePath;
    private Category category;

    public static GetShowsByConditionResponse from(ShowDetail showDetail) {
        return GetShowsByConditionResponse.builder()
            .id(showDetail.getId())
            .name(showDetail.getName())
            .runtime(showDetail.getRuntime())
            .priceBySeatClass(showDetail.getPriceBySeatClass())
            .posterImagePath(showDetail.getPosterImagePath())
            .category(showDetail.getCategory())
            .build();
    }
}
