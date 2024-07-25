package com.colorpl.show.domain.show.application;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Relate {

    private String relatenm;
    private String relateurl;

}
