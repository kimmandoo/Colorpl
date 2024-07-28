package com.colorpl.show.application;

import com.colorpl.show.infra.ShowListApiResponse;
import java.time.LocalDate;

public interface RetrieveShowListApiService {

    ShowListApiResponse retrieve(LocalDate from, LocalDate to, int page);
}
