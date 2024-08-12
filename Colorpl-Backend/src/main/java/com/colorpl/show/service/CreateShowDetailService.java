package com.colorpl.show.service;

import com.colorpl.global.common.exception.CategoryNotFoundException;
import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.SeatClass;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.domain.ShowState;
import com.colorpl.show.dto.ShowDetailApiResponse.Item;
import com.colorpl.show.repository.ShowDetailRepository;
import com.colorpl.theater.domain.Hall;
import com.colorpl.theater.repository.TheaterRepository;
import com.colorpl.theater.service.CreateTheaterService;
import com.colorpl.theater.service.GetHallByTheaterApiIdAndTheaterNameService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private final CreateTheaterService createTheaterService;
    private final ShowDetailRepository showDetailRepository;
    private final TheaterRepository theaterRepository;
    private final GetHallByTheaterApiIdAndTheaterNameService getHallByTheaterApiIdAndTheaterNameService;
    private final CreateSeatService createSeatService;

    public ShowDetail create(Item item) {

        Hall hall = getHallByTheaterApiIdAndTheaterNameService.getHallByTheaterApiIdAndTheaterName(
            item.getTheaterApiId(), item.getTheaterName());

        ShowDetail showDetail = ShowDetail.builder()
            .apiId(item.getShowApiId())
            .name(item.getName())
            .cast(item.getCast())
            .runtime(item.getRuntime())
            .priceBySeatClass(parse(item.getPriceBySeatClass()))
            .posterImagePath(item.getPosterImagePath())
            .area(item.getArea())
            .category(Category.fromString(item.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(item.getCategory())))
            .state(ShowState.from(item.getState()))
            .hall(hall)
            .build();
        createSeatService.create(showDetail);
        showDetailRepository.save(showDetail);
        return showDetail;
    }

    private Map<SeatClass, Integer> parse(String priceBySeatClass) {
        Map<SeatClass, Integer> map = new HashMap<>();
        ArrayList<Integer> prices = new ArrayList<>();
        Arrays.stream(priceBySeatClass.split(", ")).forEach(source -> {
            int index = source.lastIndexOf(" ");
            String price = source.substring(index + 1);
            if (StringUtils.hasText(price)) {
                prices.add(Integer.parseInt(price.replaceAll("[^0-9]", "")));
            } else {
                prices.add(0);
            }
        });
        prices.sort(Collections.reverseOrder());

        if (prices.size() == 1) {
            map.put(SeatClass.B, prices.get(0));
        } else if (prices.size() == 2) {
            map.put(SeatClass.A, prices.get(0));
            map.put(SeatClass.B, prices.get(1));
        } else if (prices.size() == 3) {
            map.put(SeatClass.S, prices.get(0));
            map.put(SeatClass.A, prices.get(1));
            map.put(SeatClass.B, prices.get(2));
        } else {
            map.put(SeatClass.R, prices.get(0));
            map.put(SeatClass.S, prices.get(1));
            map.put(SeatClass.A, prices.get(2));
            map.put(SeatClass.B, prices.get(3));
        }

        return map;
    }
}
