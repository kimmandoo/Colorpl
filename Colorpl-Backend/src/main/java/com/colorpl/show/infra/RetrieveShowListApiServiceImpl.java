package com.colorpl.show.infra;

import com.colorpl.show.application.RetrieveShowListApiService;
import com.colorpl.show.application.ShowListApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
@Transactional
public class RetrieveShowListApiServiceImpl implements RetrieveShowListApiService {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${show.api.key}")
    private String showApiKey;
    @Value("${show.api.url}")
    private String showApiUrl;

    @Override
    public ShowListApiResponse retrieve(LocalDate from, LocalDate to, int page) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        URI uri = UriComponentsBuilder.fromHttpUrl(showApiUrl)
            .path("pblprfr")
            .queryParam("service", showApiKey)
            .queryParam("stdate", from.format(formatter))
            .queryParam("eddate", to.format(formatter))
            .queryParam("cpage", page)
            .queryParam("rows", 10)
            .build()
            .toUri();
        String xml = webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return deserialize(xml);
    }

    private ShowListApiResponse deserialize(String xml) {
        try {
            return new XmlMapper().readValue(xml, ShowListApiResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
