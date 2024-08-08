package com.colorpl.theater.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import lombok.Getter;

@Getter
@JacksonXmlRootElement(localName = "dbs")
public class RetrieveTheaterDetailApiResponse {

    @JacksonXmlProperty(localName = "db")
    private Db theaterDetail;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Db {

        @JacksonXmlProperty(localName = "fcltynm")
        private String name;

        @JacksonXmlProperty(localName = "mt10id")
        private String apiId;

        @JacksonXmlProperty(localName = "seatscale")
        private int seatscale;

        @JacksonXmlProperty(localName = "adres")
        private String address;

        @JacksonXmlProperty(localName = "la")
        private double latitude;

        @JacksonXmlProperty(localName = "lo")
        private double longitude;

        @JacksonXmlElementWrapper(localName = "mt13s")
        @JacksonXmlProperty(localName = "mt13")
        private List<Mt13> mt13s;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Mt13 {

            @JacksonXmlProperty(localName = "prfplcnm")
            private String name;

            @JacksonXmlProperty(localName = "mt13id")
            private String apiId;
        }
    }
}
