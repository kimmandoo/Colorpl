package com.colorpl.show.application;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowDetailContainer {

    private ShowDetail db;

}
