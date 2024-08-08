package com.colorpl.show.service;

import com.colorpl.show.dto.ShowListApiResponse;
import java.time.LocalDate;

public interface RetrieveShowListApiService {

    ShowListApiResponse retrieve(LocalDate from, LocalDate to, int page);
}
