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

@Component
@RequiredArgsConstructor
public class StatisticsResponseCollector implements Collector<StatisticBucket,
    StatisticsResponseCollector, StatisticsResponse> {

    private final Utils utils;

    private StatisticBucket bucket;

    private Object lock = new Object();

    @Override
    public Supplier<StatisticsResponseCollector> supplier() {

        this.bucket = null;
        return () -> this;
    }

    @Override
    public BiConsumer<StatisticsResponseCollector, StatisticBucket> accumulator() {

        return (builder, otherBucket) -> {

            synchronized (lock) {
                builder.bucket = merger(builder.bucket, otherBucket);
            }
        };
    }

    @Override
    public BinaryOperator<StatisticsResponseCollector> combiner() {

        return (builder, other) -> {

            synchronized (lock) {
                builder.bucket = merger(builder.bucket, other.bucket);
            }

            return builder;
        };
    }

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

    @Override
    public Set<Characteristics> characteristics() {

        return Sets.immutableEnumSet(Characteristics.UNORDERED, Characteristics.CONCURRENT);
    }

    public static StatisticsResponseCollector toStatisticsResponse(Utils utils) {
        return new StatisticsResponseCollector(utils);
    }
}
