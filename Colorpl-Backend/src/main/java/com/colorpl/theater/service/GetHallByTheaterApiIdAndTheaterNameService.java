package com.colorpl.theater.service;

import com.colorpl.global.common.exception.HallNotFoundException;
import com.colorpl.theater.domain.Hall;
import com.colorpl.theater.domain.Theater;
import com.colorpl.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetHallByTheaterApiIdAndTheaterNameService {

    private final TheaterRepository theaterRepository;
    private final CreateTheaterService createTheaterService;

    @Transactional(readOnly = true)
    public Hall getHallByTheaterApiIdAndTheaterName(String theaterApiId, String theaterName) {

        Theater theater = theaterRepository.findByApiId(theaterApiId)
            .orElseGet(() -> createTheaterService.createTheaterByApiId(theaterApiId));

        for (Hall hall : theater.getHalls()) {
            if (theaterName.contains(hall.getName())) {
                return hall;
            }
        }

        throw new HallNotFoundException(theaterName);
    }
}
