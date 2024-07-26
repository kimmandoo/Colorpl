package com.colorpl.show.domain.show.application;

import com.colorpl.show.domain.show.domain.Show;
import com.colorpl.show.domain.show.domain.ShowRepository;
import com.colorpl.show.domain.show.domain.ShowState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateShowService {

    private final ShowRepository showRepository;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://kopis.or.kr/openApi/restful/pblprfr")
            .build();

    @Value("${show.api.key}")
    private String showApiKey;

    public Show createShow(String mt20id) {

        String xml = retrieveShowDetail(mt20id);
        ShowDetail showDetail = deserializeShowDetail(xml);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        Map<String, Integer> priceBySeatClass = priceBySeatClassFromString(showDetail.getPcseguidance());

        Show show = Show.builder()
                .name(showDetail.getPrfnm())
                .priceBySeatClass(priceBySeatClass)
                .startDate(LocalDate.parse(showDetail.getPrfpdfrom(), formatter))
                .endDate(LocalDate.parse(showDetail.getPrfpdto(), formatter))
                .cast(showDetail.getPrfcast())
                .runtime(showDetail.getPrfruntime())
                .posterImagePath(showDetail.getPoster())
                .area(showDetail.getArea())
                .genre(showDetail.getGenrenm())
                .state(ShowState.fromString(showDetail.getPrfstate()))
                .schedule(showDetail.getDtguidance())
                .build();

        showRepository.save(show);
        return show;
    }

    private String retrieveShowDetail(String mt20id) {
        return webClient.get()
                .uri("/{mt20id}?service={service}&newsql=Y", mt20id, showApiKey)
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

    private Map<String, Integer> priceBySeatClassFromString(String priceString) {
        Map<String, Integer> priceBySeatClass = new HashMap<>();
        Arrays.stream(priceString.split(", "))
                .map(string -> string.split(" "))
                .forEach(arr -> {
                    if (arr.length == 1)
                        priceBySeatClass.put(arr[0], 0);
                    else
                        priceBySeatClass.put(arr[0], Integer.parseInt(arr[1].replaceAll("[^0-9]", "")));
                });
        return priceBySeatClass;
    }

}
