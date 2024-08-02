package com.colorpl.theater.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class TheaterDetailApiResponse {

    @JacksonXmlProperty(localName = "db")
    private Item item;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        @JacksonXmlProperty(localName = "fcltynm")
        private String name;

        @JacksonXmlProperty(localName = "mt10id")
        private String apiId;

        @JacksonXmlProperty(localName = "adres")
        private String address;

        @JacksonXmlProperty(localName = "la")
        private String latitude;

        @JacksonXmlProperty(localName = "lo")
        private String longitude;
    }
}
