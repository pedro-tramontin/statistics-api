package com.n26.statistics.api.service.impl;


import static com.n26.statistics.api.service.impl.StatisticsResponseCollector.toStatisticsResponse;

import com.n26.statistics.api.controller.request.TransactionRequest;
import com.n26.statistics.api.controller.response.StatisticsResponse;
import com.n26.statistics.api.misc.Utils;
import com.n26.statistics.api.service.StatisticsService;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final Map<Long, StatisticBucket> buckets;

    private final Utils utils;

    @Value("${app.transaction.timestamp.min-past-interval}")
    private int maxInterval;

    private Object lock = new Object();

    @Override
    public void put(TransactionRequest transactionRequest) {

        long bucketKey = utils.mapKey(transactionRequest.getTimestamp());

        StatisticBucket newBucket = new StatisticBucket(transactionRequest.getAmount());

        buckets.merge(bucketKey, newBucket, StatisticBucket::merger);
    }

    @Override
    public StatisticsResponse getStatistics(Instant now) {

        return IntStream.range(0, maxInterval)
            .boxed()
            .map(i -> buckets.get(utils.mapKey(now) - i))
            .filter(Objects::nonNull)
            .collect(toStatisticsResponse(utils));

    }

}
