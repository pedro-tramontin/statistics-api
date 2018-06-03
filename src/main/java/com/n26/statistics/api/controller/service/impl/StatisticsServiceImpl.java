package com.n26.statistics.api.controller.service.impl;

import static com.n26.statistics.api.controller.service.impl.StatisticsResponseCollector.toStatisticsResponse;

import com.n26.statistics.api.Utils;
import com.n26.statistics.api.controller.request.TransactionRequest;
import com.n26.statistics.api.controller.response.StatisticsResponse;
import com.n26.statistics.api.controller.service.StatisticsService;

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

    @Override
    public void put(TransactionRequest transactionRequest) {

        long bucketKey = utils.mapKey(transactionRequest.getTimestamp());

        double amount = transactionRequest.getAmount();

        StatisticBucket bucket = buckets.get(bucketKey);
        if (bucket != null) {
            bucket.add(amount);
        } else {
            buckets.put(bucketKey, new StatisticBucket(amount));
        }
    }

    @Override
    public StatisticsResponse getStatistics(Instant now) {

        return IntStream.range(0, maxInterval - 1)
            .boxed()
            .map(i -> buckets.get(utils.mapKey(now) - i))
            .filter(Objects::nonNull)
            .collect(toStatisticsResponse());

    }

}
