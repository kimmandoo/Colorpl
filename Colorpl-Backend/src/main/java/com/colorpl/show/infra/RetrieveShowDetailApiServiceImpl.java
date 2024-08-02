package com.colorpl.show.infra;

import com.colorpl.show.application.RetrieveShowDetailApiService;
import com.colorpl.show.application.ShowDetailApiResponse;
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
public class RetrieveShowDetailApiServiceImpl implements RetrieveShowDetailApiService {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${show.api.key}")
    private String showApiKey;
    @Value("${show.api.url}")
    private String showApiUrl;

    @Override
    public ShowDetailApiResponse retrieve(String apiId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(showApiUrl)
            .path("pblprfr/")
            .path(apiId)
            .queryParam("service", showApiKey)
            .queryParam("newsql", "Y")
            .build()
            .toUri();
        String xml = webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return deserialize(xml);
    }

    private ShowDetailApiResponse deserialize(String xml) {
        try {
            return new XmlMapper().readValue(xml, ShowDetailApiResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
