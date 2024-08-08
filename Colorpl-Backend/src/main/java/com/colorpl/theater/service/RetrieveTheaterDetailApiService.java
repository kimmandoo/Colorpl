package com.colorpl.theater.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class RetrieveTheaterDetailApiService {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${show.api.key}")
    private String showApiKey;
    @Value("${show.api.url}")
    private String showApiUrl;

    @Transactional
    public RetrieveTheaterDetailApiResponse retrieve(String apiId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(showApiUrl)
            .path("/prfplc")
            .path("/{apiId}")
            .queryParam("service", showApiKey)
            .buildAndExpand(apiId)
            .toUri();

        String xml = webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return deserialize(xml);
    }

    private RetrieveTheaterDetailApiResponse deserialize(String xml) {
        try {
            return new XmlMapper().readValue(xml, RetrieveTheaterDetailApiResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
