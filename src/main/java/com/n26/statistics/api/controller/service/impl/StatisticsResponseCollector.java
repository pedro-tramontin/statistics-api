package com.n26.statistics.api.controller.service.impl;

import com.n26.statistics.api.controller.response.StatisticsResponse;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class StatisticsResponseCollector implements Collector<StatisticBucket,
    StatisticsResponseCollector, StatisticsResponse> {

    private StatisticBucket bucket;

    @Override
    public Supplier<StatisticsResponseCollector> supplier() {
        return StatisticsResponseCollector::new;
    }

    @Override
    public BiConsumer<StatisticsResponseCollector, StatisticBucket> accumulator() {

        return (builder, bucket) -> {
            if (builder.bucket == null) {
                builder.bucket = bucket;
            } else {
                builder.bucket.merge(bucket);
            }
        };
    }

    @Override
    public BinaryOperator<StatisticsResponseCollector> combiner() {

        return (builder, other) -> {
            if (builder.bucket == null) {
                builder.bucket = other.bucket;
            } else {
                builder.bucket.merge(other.bucket);
            }

            return builder;
        };
    }

    @Override
    public Function<StatisticsResponseCollector, StatisticsResponse> finisher() {

        return (builder) -> {

            if (builder.bucket != null) {
                return new StatisticsResponse(
                    builder.bucket.getSum(),
                    builder.bucket.getAvg(),
                    builder.bucket.getMax(),
                    builder.bucket.getMin(),
                    builder.bucket.getCount()
                );
            } else {
                return new StatisticsResponse(0.0, 0.0, 0.0, 0.0, 0L);
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {

        return Sets.immutableEnumSet(Characteristics.UNORDERED);
    }

    public static StatisticsResponseCollector toStatisticsResponse() {

        return new StatisticsResponseCollector();
    }

}
