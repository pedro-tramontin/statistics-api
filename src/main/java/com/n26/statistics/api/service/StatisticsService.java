package com.n26.statistics.api.service;

import com.n26.statistics.api.controller.request.TransactionRequest;
import com.n26.statistics.api.controller.response.StatisticsResponse;

import java.time.Instant;

public interface StatisticsService {

    void put(TransactionRequest transactionRequest);

    StatisticsResponse getStatistics(Instant now);
}
