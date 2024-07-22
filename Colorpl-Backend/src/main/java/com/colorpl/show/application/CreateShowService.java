package com.colorpl.show.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateShowService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://kopis.or.kr/openApi/restful/pblprfr")
            .build();

    private String retrieveShowDetail(String mt20id, String service) {
        return webClient.get()
                .uri("/{mt20id}?service={service}&newsql=Y", mt20id, service)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private ShowDetail deserializeShowDetail(String xml) {
        XmlMapper xmlMapper = new XmlMapper();
        ShowDetailContainer showDetailContainer = null;
        try {
            showDetailContainer = xmlMapper.readValue(xml, ShowDetailContainer.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return showDetailContainer.getDb();
    }

}
