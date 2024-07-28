package com.colorpl.show.application;

import java.time.LocalDate;

public interface RetrieveShowListApiService {

    ShowListApiResponse retrieve(LocalDate from, LocalDate to, int page);
}
