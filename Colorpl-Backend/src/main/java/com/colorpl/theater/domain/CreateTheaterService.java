package com.colorpl.theater.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateTheaterService {

    private final RetrieveTheaterDetailApiService retrieveTheaterDetailApiService;
    private final TheaterRepository theaterRepository;

    public Theater create(String apiId) {
        TheaterDetailApiResponse response = retrieveTheaterDetailApiService.retrieve(apiId);
        Theater theater = Theater.builder()
            .apiId(response.getItem().getApiId())
            .name(response.getItem().getName())
            .address(response.getItem().getAddress())
            .latitude(Double.valueOf(response.getItem().getLatitude()))
            .longitude(Double.valueOf(response.getItem().getLongitude()))
            .build();
        return theaterRepository.save(theater);
    }
}
