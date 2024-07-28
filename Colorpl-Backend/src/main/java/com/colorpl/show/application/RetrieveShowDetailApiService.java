package com.colorpl.show.application;

import com.colorpl.show.infra.ShowDetailApiResponse;

public interface RetrieveShowDetailApiService {

    ShowDetailApiResponse retrieve(String apiId);
}
