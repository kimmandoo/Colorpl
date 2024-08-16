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
public class GetHallService {

    private final TheaterRepository theaterRepository;
    private final CreateTheaterService createTheaterService;

    @Transactional
    public Hall getHall(String theaterApiId, String theaterName) {
        Theater theater = theaterRepository.findByApiId(theaterApiId)
            .orElseGet(() -> createTheaterService.createTheaterByApiId(theaterApiId));
        return theater.getHalls().stream()
            .filter(hall -> theaterName.contains(hall.getName()))
            .findAny()
            .orElseThrow(() -> new HallNotFoundException(theaterName));
    }
}
