package com.colorpl.show.domain.show.application;

import lombok.*;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowDetail {

    private String mt20id;
    private String prfnm;
    private String prfpdfrom;
    private String prfpdto;
    private String fcltynm;
    private String prfcast;
    private String prfcrew;
    private String prfruntime;
    private String prfage;
    private String entrpsnm;
    private String entrpsnmP;
    private String entrpsnmA;
    private String entrpsnmH;
    private String entrpsnmS;
    private String pcseguidance;
    private String poster;
    private String sty;
    private String area;
    private String genrenm;
    private String openrun;
    private String visit;
    private String child;
    private String daehakro;
    private String festival;
    private String musicallicense;
    private String musicalcreate;
    private String updatedate;
    private String prfstate;
    private List<String> styurls;
    private String mt10id;
    private String dtguidance;
    private List<Relate> relates;

}
