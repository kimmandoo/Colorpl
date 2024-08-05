package com.colorpl.show.domain.detail;

import com.colorpl.show.application.ShowDetailApiResponse.Item;
import com.colorpl.theater.domain.CreateTheaterService;
import com.colorpl.theater.domain.Theater;
import com.colorpl.theater.domain.TheaterRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateShowDetailService {

    private final CreateSeatService createSeatService;
    private final CreateTheaterService createTheaterService;
    private final ShowDetailRepository showDetailRepository;
    private final TheaterRepository theaterRepository;

    public ShowDetail create(Item item) {
        int pos = item.getHall().lastIndexOf('(');
        String hall = item.getHall().substring(pos + 1, item.getHall().length() - 1);
        Optional<Theater> optional = theaterRepository.findByApiId(item.getTheaterApiId());
        Theater theater = optional.isPresent() ? optional.get()
            : createTheaterService.create(item.getTheaterApiId());

        ShowDetail showDetail = ShowDetail.builder()
            .apiId(item.getShowApiId())
            .name(item.getName())
            .cast(item.getCast())
            .runtime(item.getRuntime())
            .priceBySeatClass(parse(item.getPriceBySeatClass()))
            .posterImagePath(item.getPosterImagePath())
            .area(item.getArea())
            .state(ShowState.from(item.getState()))
//            .theater(theater)
            .hall(hall)
            .build();
        createSeatService.create(showDetail);
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
