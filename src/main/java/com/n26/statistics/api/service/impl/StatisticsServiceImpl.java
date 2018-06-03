package com.n26.statistics.api.service.impl;


import static com.n26.statistics.api.service.impl.StatisticsResponseCollector.toStatisticsResponse;

import com.n26.statistics.api.controller.request.TransactionRequest;
import com.n26.statistics.api.controller.response.StatisticsResponse;
import com.n26.statistics.api.misc.Utils;
import com.n26.statistics.api.service.StatisticsService;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementation for the {@link StatisticsService}.
 */
@RequiredArgsConstructor
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final Map<Long, StatisticBucket> buckets;

    private final Utils utils;

    @Value("${app.transaction.timestamp.min-past-interval}")
    private int minPastInterval;

    @Override
    public void put(TransactionRequest transactionRequest) {

        long bucketKey = utils.mapKey(transactionRequest.getTimestamp());

        StatisticBucket newBucket = new StatisticBucket(transactionRequest.getAmount());

        buckets.merge(bucketKey, newBucket, StatisticBucket::merger);
    }

    @Override
    public StatisticsResponse getStatistics(Instant now) {

        return IntStream.range(0, minPastInterval)
            .boxed()
            .parallel()
            .map(bucketAtInstantNowLessPassedSecond(now))
            .filter(Objects::nonNull)
            .collect(toStatisticsResponse(utils));

    }

    /**
     * Returns a lambda that encapsulates the now and returns a bucket based in the seconds value
     * that is passed to it.
     *
     * For instance, to get the most recent bucket, pass the value 0 -> because it will get the
     * bucket for the instant: now - 0 = now.
     *
     * And for instance, to get the older bucket, pass the value 59 -> because it will get the
     * bucket for the instant: now - 59.
     */
    private Function<Integer, StatisticBucket> bucketAtInstantNowLessPassedSecond(Instant now) {

        final long timestamp = utils.mapKey(now);

        return second -> buckets.get(timestamp - second);
    }
}
