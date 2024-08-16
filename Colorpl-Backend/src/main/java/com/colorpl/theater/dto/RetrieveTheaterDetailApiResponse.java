package com.colorpl.theater.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "dbs")
public class RetrieveTheaterDetailApiResponse {

    @JacksonXmlProperty(localName = "db")
    private Theater theater;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Theater {

        @JacksonXmlProperty(localName = "fcltynm")
        private String name;

        @JacksonXmlProperty(localName = "mt10id")
        private String apiId;

        @JacksonXmlProperty(localName = "adres")
        private String address;

        @JacksonXmlProperty(localName = "la")
        private Double latitude;

        @JacksonXmlProperty(localName = "lo")
        private Double longitude;

        @JacksonXmlElementWrapper(localName = "mt13s")
        @JacksonXmlProperty(localName = "mt13")
        private final List<Hall> halls = new ArrayList<>();

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Hall {

            @JacksonXmlProperty(localName = "prfplcnm")
            private String name;

            @JacksonXmlProperty(localName = "mt13id")
            private String apiId;
        }
    }
}
