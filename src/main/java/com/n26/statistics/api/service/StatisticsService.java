package com.n26.statistics.api.service;

import com.n26.statistics.api.controller.request.TransactionRequest;
import com.n26.statistics.api.controller.response.StatisticsResponse;

import java.time.Instant;

/**
 * Basic interface with methods to insert bucketAtInstantLessSecond transaction and generate statistics for all the
 * transactions of the last 60 seconds.
 */
public interface StatisticsService {

    /**
     * Stores bucketAtInstantLessSecond transaction that will be used to generate statistics
     */
    void put(TransactionRequest transactionRequest);

    /**
     * Generates the statistics for the last 60 seconds until the instant now
     */
    StatisticsResponse getStatistics(Instant now);
}
