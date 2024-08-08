package com.colorpl.show.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CreateByDateRequest {

    private LocalDate from;
    private LocalDate to;
}
