package com.n26.statistics.api.controller.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StatisticBucket {

    private double sum;

    private double max;

    private double min;

    private long count;

    public StatisticBucket(double sum) {
        this.sum = sum;
        this.max = sum;
        this.min = sum;
        this.count = 1L;
    }

    public void add(double sum) {
        updateSum(sum);
        updateMaxIfGreater(sum);
        updateMinIfLesser(sum);
        updateCount(1);
    }

    public StatisticBucket merge(StatisticBucket other) {
        updateSum(other.getSum());
        updateMaxIfGreater(other.getMax());
        updateMinIfLesser(other.getMin());
        updateCount(other.getCount());

        return this;
    }

    public double getAvg() {
        if (this.count == 0.0) {
            return 0.0;
        }

        return this.sum / this.count;
    }

    private void updateSum(double sum) {
        this.sum += sum;
    }

    private void updateMaxIfGreater(double sum) {
        if (max < sum) {
            this.max = sum;
        }
    }

    private void updateMinIfLesser(double sum) {
        if (min > sum) {
            this.min = sum;
        }
    }

    private void updateCount(long count) {
        this.count += count;
    }
}
