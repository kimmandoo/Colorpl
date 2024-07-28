package com.colorpl.show.domain.detail;

import com.colorpl.show.application.ShowDetailApiResponse.Item;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateShowDetailService {

    private final ShowDetailRepository showDetailRepository;

    public ShowDetail create(Item item) {
        ShowDetail showDetail = ShowDetail.builder()
            .apiId(item.getApiId())
            .name(item.getName())
            .cast(item.getCast())
            .runtime(item.getRuntime())
            .priceBySeatClass(parse(item.getPriceBySeatClass()))
            .posterImagePath(item.getPosterImagePath())
            .area(item.getArea())
            .state(ShowState.from(item.getState()))
            .build();
        showDetailRepository.save(showDetail);
        return showDetail;
    }

    private Map<String, Integer> parse(String priceBySeatClass) {
        Map<String, Integer> map = new HashMap<>();
        Arrays.stream(priceBySeatClass.split(", ")).forEach(source -> {
            int index = source.lastIndexOf(" ");
            String seatClass = source.substring(0, index);
            String price = source.substring(index + 1);
            if (StringUtils.hasText(price)) {
                map.put(seatClass, Integer.parseInt(price.replaceAll("[^0-9]", "")));
            } else {
                map.put(seatClass, 0);
            }
        });
        return map;
    }
}
