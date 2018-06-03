package com.n26.statistics.api.service.impl;

import static com.n26.statistics.api.misc.Utils.ZERO;
import static com.n26.statistics.api.service.impl.StatisticBucket.merger;

import com.n26.statistics.api.controller.response.StatisticsResponse;
import com.n26.statistics.api.misc.Utils;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Helper collector to merge a list of {@link StatisticBucket}
 */
@Component
@RequiredArgsConstructor
public class StatisticsResponseCollector implements Collector<StatisticBucket,
    StatisticsResponseCollector, StatisticsResponse> {

    private final Utils utils;

    /**
     * Internal bucket
     */
    private StatisticBucket bucket;

    private final Object lock = new Object();

    /**
     * Lambda to return this instance
     */
    @Override
    public Supplier<StatisticsResponseCollector> supplier() {

        this.bucket = null;
        return () -> this;
    }

    /**
     * Lambda to add a bucket to this collector
     */
    @Override
    public BiConsumer<StatisticsResponseCollector, StatisticBucket> accumulator() {

        return (builder, otherBucket) -> {

            // Sincronizes the merge to enable concurrent access to this collector
            synchronized (lock) {
                builder.bucket = merger(builder.bucket, otherBucket);
            }
        };
    }

    /**
     * Lambda that combines two collectors together
     */
    @Override
    public BinaryOperator<StatisticsResponseCollector> combiner() {

        return (builder, other) -> {

            // Sincronizes the merge to enable concurrent access to this collector
            synchronized (lock) {
                builder.bucket = merger(builder.bucket, other.bucket);
            }

            return builder;
        };
    }

    /**
     * Lambda to create the {@link StatisticsResponse} based on the internal {@link
     * StatisticBucket}
     */
    @Override
    public Function<StatisticsResponseCollector, StatisticsResponse> finisher() {

        return builder -> {

            if (builder.bucket != null) {

                return new StatisticsResponse(
                    utils.toBigDecimalScaledToTwo(builder.bucket.getSum()),
                    utils.toBigDecimalScaledToTwo(builder.bucket.getAvg()),
                    utils.toBigDecimalScaledToTwo(builder.bucket.getMax()),
                    utils.toBigDecimalScaledToTwo(builder.bucket.getMin()),
                    builder.bucket.getCount()
                );
            } else {

                return new StatisticsResponse(ZERO, ZERO, ZERO, ZERO, 0L);
            }
        };
    }

    /**
     * This collector is {@link java.util.stream.Collector.Characteristics#UNORDERED} and {@link
     * java.util.stream.Collector.Characteristics#CONCURRENT}
     */
    @Override
    public Set<Characteristics> characteristics() {

        return Sets.immutableEnumSet(Characteristics.UNORDERED, Characteristics.CONCURRENT);
    }

    /**
     * Creates a new instance of this collector
     */
    static StatisticsResponseCollector toStatisticsResponse(Utils utils) {
        return new StatisticsResponseCollector(utils);
    }
}
