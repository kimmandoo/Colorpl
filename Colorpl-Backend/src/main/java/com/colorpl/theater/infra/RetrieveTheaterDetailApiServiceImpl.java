package com.colorpl.theater.infra;

import com.colorpl.theater.domain.RetrieveTheaterDetailApiService;
import com.colorpl.theater.domain.TheaterDetailApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
@Transactional
public class RetrieveTheaterDetailApiServiceImpl implements RetrieveTheaterDetailApiService {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${show.api.key}")
    private String showApiKey;
    @Value("${show.api.url}")
    private String showApiUrl;

    @Override
    public TheaterDetailApiResponse retrieve(String apiId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(showApiUrl).path("prfplc/").path(apiId)
            .queryParam("service", showApiKey).build().toUri();
        String xml = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
        return deserialize(xml);
    }

    private TheaterDetailApiResponse deserialize(String xml) {
        try {
            return new XmlMapper().readValue(xml, TheaterDetailApiResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
