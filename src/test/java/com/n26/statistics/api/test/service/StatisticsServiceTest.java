package com.n26.statistics.api.test.service;

import static com.n26.statistics.api.test.utils.FakeData.randomAmount;
import static com.n26.statistics.api.test.utils.FakeData.randomTime;
import static com.n26.statistics.api.test.utils.MeasureUtils.measureXTimesExecution;
import static org.springframework.util.Assert.isTrue;

import com.n26.statistics.api.controller.request.TransactionRequest;
import com.n26.statistics.api.controller.response.StatisticsResponse;
import com.n26.statistics.api.service.StatisticsService;
import com.n26.statistics.api.service.impl.StatisticBucket;
import com.n26.statistics.api.test.utils.StopWatch;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private Map<Long, StatisticBucket> buckets;

    @Value("${app.transaction.timestamp.min-past-interval}")
    private int minPastInterval;

    @Test
    public void putTransaction() {

        double amount = randomAmount();

        statisticsService.put(new TransactionRequest(amount, Instant.now()));
    }

    @Test
    public void emptyStatistics() {

        buckets.clear();

        StatisticsResponse stats = statisticsService.getStatistics(Instant.now());

        isTrue(stats.getSum().doubleValue() == 0.00, "sum is wrong");
        isTrue(stats.getAvg().doubleValue() == 0.00, "avg is wrong");
        isTrue(stats.getMax().doubleValue() == 0.00, "max is wrong");
        isTrue(stats.getMin().doubleValue() == 0.00, "min is wrong");
        isTrue(stats.getCount().doubleValue() == 0L, "count is wrong");

        isTrue(buckets.size() == 0, "buckets should be empty");
    }

    @Test
    public void statisticsFrom60Buckets() {

        Instant now = Instant.now();
        double amount = 12.5;

        loadDataIn60Buckets(now, amount);

        long expCount = 60;
        double expSum = amount * expCount;
        double expMax = amount;
        double expMin = amount;
        double expAvg = expSum / expCount;

        StatisticsResponse stats = statisticsService.getStatistics(now);

        isTrue(stats.getSum().doubleValue() == expSum, "sum is wrong");
        isTrue(stats.getAvg().doubleValue() == expAvg, "avg is wrong");
        isTrue(stats.getMax().doubleValue() == expMax, "max is wrong");
        isTrue(stats.getMin().doubleValue() == expMin, "min is wrong");
        isTrue(stats.getCount().doubleValue() == expCount, "count is wrong");

        isTrue(buckets.size() == 60, "buckets should have 60 items");
    }

    @Test
    public void constantTimeForStatistics() {

        Instant now = Instant.now();
        Instant minTime = now.minusSeconds(minPastInterval - 1);
        double amount = randomAmount();

        loadDataIn60Buckets(now, amount);
        long average = measureAverageTimeForStatistics(now);

        put1KRandomTransaction(minTime, now);
        long avg1K = measureAverageTimeForStatistics(now);

        isTrue(isLess(avg1K, average), "average for 1K is way longer than for 60 items");

        put1MRandomTransaction(minTime, now);
        long avg1M = measureAverageTimeForStatistics(now);

        isTrue(isLess(avg1K, average), "average for 1M is way longer than for 60 items");

        isTrue(buckets.size() == 60, "buckets should have 60 items");
    }

    //////////
    // Auxiliary methods used to store transactions and test average time for statistics retrieval
    //
    private boolean isLess(final long valueNano, final long avgNano) {

        Duration value = Duration.of(valueNano, ChronoUnit.NANOS);
        Duration max = Duration.of(avgNano, ChronoUnit.NANOS).plusSeconds(1);

        return value.compareTo(max) < 0;
    }

    private void loadDataIn60Buckets(Instant now, double amount) {
        buckets.clear();
        for (int i = 0; i < 60; i++) {
            statisticsService.put(new TransactionRequest(amount, now.minusSeconds(i)));
        }
    }

    private void put1MRandomTransaction(Instant from, Instant to) {
        for (int i = 0; i < 1000; i++) {
            put1KRandomTransaction(from, to);
        }
    }

    private void put1KRandomTransaction(Instant from, Instant to) {
        for (int i = 0; i < 1000; i++) {
            putRandomTransaction(from, to);
        }
    }

    private void putRandomTransaction(Instant from, Instant to) {
        statisticsService.put(new TransactionRequest(randomAmount(), randomTime(from, to)));
    }

    private long measureAverageTimeForStatistics(Instant now) {
        return measureXTimesExecution(10, () -> measure100Times(now));
    }

    private long measure100Times(Instant now) {
        return measureXTimesExecution(100, () -> measureGetStatisticsTime(now));
    }

    private long measureGetStatisticsTime(Instant now) {

        StopWatch.start();
        statisticsService.getStatistics(now);

        return StopWatch.get();
    }

}
