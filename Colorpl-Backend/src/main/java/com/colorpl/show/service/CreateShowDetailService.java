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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
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
            .priceBySeatClass(parsePriceBySeatClass(item.getPriceBySeatClass()))
            .posterImagePath(item.getPosterImagePath())
            .area(item.getArea())
            .category(category)
            .state(state)
            .build();
        showDetailRepository.save(showDetail);
        createSeatService.createSeat(showDetail);
        return showDetail;
    }

    private Map<SeatClass, Integer> parsePriceBySeatClass(String source) {
        List<Integer> prices = Arrays.stream(source.split(", "))
            .map(this::parsePrice)
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

    private Integer parsePrice(String source) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(source);
        StringBuilder price = new StringBuilder();
        while (matcher.find()) {
            price.append(matcher.group());
        }
        if (StringUtils.hasText(price)) {
            return Integer.valueOf(price.toString());
        }
        return 0;
    }
}
