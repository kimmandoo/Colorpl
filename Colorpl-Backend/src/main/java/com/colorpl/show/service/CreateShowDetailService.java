package com.colorpl.show.service;

import com.colorpl.global.common.exception.CategoryNotFoundException;
import com.colorpl.global.common.exception.InvalidSeatClassException;
import com.colorpl.global.common.exception.ShowStateNotFoundException;
import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.SeatClass;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.domain.ShowState;
import com.colorpl.show.dto.ShowDetailApiResponse.Item;
import com.colorpl.show.repository.ShowDetailRepository;
import com.colorpl.theater.domain.Hall;
import com.colorpl.theater.service.GetHallService;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CreateShowDetailService {

    private final GetHallService getHallService;
    private final CreateSeatService createSeatService;
    private final ShowDetailRepository showDetailRepository;

    @Transactional
    public ShowDetail createShowDetail(Item item) {
        Hall hall = getHallService.getHall(item.getTheaterApiId(), item.getTheaterName());
        Category category = Category.fromString(item.getCategory())
            .orElseThrow(() -> new CategoryNotFoundException(item.getCategory()));
        ShowState state = ShowState.fromString(item.getState())
            .orElseThrow(() -> new ShowStateNotFoundException(item.getState()));
        ShowDetail showDetail = ShowDetail.builder()
            .apiId(item.getShowApiId())
            .name(item.getName())
            .hall(hall)
            .cast(item.getCast())
            .runtime(item.getRuntime())
            .priceBySeatClass(parsePricesBySeatClass(item.getPriceBySeatClass()))
            .posterImagePath(item.getPosterImagePath())
            .area(item.getArea())
            .category(category)
            .state(state)
            .build();
        createSeatService.createSeat(showDetail);
        showDetailRepository.save(showDetail);
        return showDetail;
    }

    private Map<SeatClass, Integer> parsePricesBySeatClass(String source) {
        List<Integer> prices = Arrays.stream(source.split(", "))
            .map(this::parsePriceBySeatClass)
            .sorted()
            .limit(4)
            .toList();
        return IntStream.range(0, prices.size())
            .boxed()
            .collect(Collectors.toMap(
                numberOfSeatClasses -> SeatClass.fromInteger(numberOfSeatClasses)
                    .orElseThrow(() -> new InvalidSeatClassException(numberOfSeatClasses)),
                prices::get
            ));
    }

    private Integer parsePriceBySeatClass(String source) {
        String price = source.substring(source.indexOf(" ") + 1, source.length() - 1);
        if (StringUtils.hasText(price)) {
            NumberStyleFormatter formatter = new NumberStyleFormatter();
            try {
                return formatter.parse(price, Locale.KOREA).intValue();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }
}
