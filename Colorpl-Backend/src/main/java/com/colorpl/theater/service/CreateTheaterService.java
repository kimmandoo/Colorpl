package com.colorpl.theater.service;

import com.colorpl.global.common.exception.TheaterAlreadyExistsException;
import com.colorpl.theater.domain.Hall;
import com.colorpl.theater.domain.Theater;
import com.colorpl.theater.dto.RetrieveTheaterDetailApiResponse;
import com.colorpl.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTheaterService {

    private final RetrieveTheaterDetailApiService retrieveTheaterDetailApiService;
    private final TheaterRepository theaterRepository;

    @Transactional
    public Theater createTheaterByApiId(String apiId) {

        if (theaterRepository.existsByApiId(apiId)) {
            throw new TheaterAlreadyExistsException(apiId);
        }

        RetrieveTheaterDetailApiResponse response = retrieveTheaterDetailApiService.retrieve(apiId);

        Theater theater = Theater.builder()
            .name(response.getTheater().getName())
            .apiId(response.getTheater().getApiId())
            .address(response.getTheater().getAddress())
            .latitude(response.getTheater().getLatitude())
            .longitude(response.getTheater().getLongitude())
            .build();

        response.getTheater().getHalls().forEach(
            h -> Hall.builder()
                .theater(theater)
                .name(h.getName())
                .apiId(h.getApiId())
                .build()
        );

        return theaterRepository.save(theater);
    }
}
