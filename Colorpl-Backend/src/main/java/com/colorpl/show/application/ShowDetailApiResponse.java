package com.colorpl.show.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowDetailApiResponse {

    @JacksonXmlProperty(localName = "db")
    private Item item;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Item {

        @JacksonXmlProperty(localName = "mt20id")
        private String showApiId;

        @JacksonXmlProperty(localName = "prfnm")
        private String name;

        @JacksonXmlProperty(localName = "prfpdfrom")
        private String startDate;

        @JacksonXmlProperty(localName = "prfpdto")
        private String endDate;

        @JacksonXmlProperty(localName = "prfcast")
        private String cast;

        @JacksonXmlProperty(localName = "prfruntime")
        private String runtime;

        @JacksonXmlProperty(localName = "pcseguidance")
        private String priceBySeatClass;

        @JacksonXmlProperty(localName = "poster")
        private String posterImagePath;

        @JacksonXmlProperty(localName = "area")
        private String area;

        @JacksonXmlProperty(localName = "prfstate")
        private String state;

        @JacksonXmlProperty(localName = "mt10id")
        private String theaterApiId;

        @JacksonXmlProperty(localName = "dtguidance")
        private String schedule;

        @JacksonXmlProperty(localName = "fcltynm")
        private String hall;
    }
}
