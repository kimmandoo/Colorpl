package com.colorpl.show.ui;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CreateByDateRequest {

    private LocalDate from;
    private LocalDate to;
}
