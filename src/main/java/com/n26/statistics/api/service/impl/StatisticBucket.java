package com.n26.statistics.api.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The bucket that is inserted in the memory map.
 *
 * Each bucket represents all the transactions that happened in a specific second.
 */
@AllArgsConstructor
@Data
public class StatisticBucket {

    private double sum;

    private double max;

    private double min;

    private long count;

    /**
     * Creates a bucket with a initial amount value.
     */
    StatisticBucket(double amount) {
        this.sum = amount;
        this.max = amount;
        this.min = amount;
        this.count = 1;
    }

    /**
     * Merges a bucket with another bucket.
     */
    private StatisticBucket merge(StatisticBucket other) {
        updateSum(other.getSum());
        updateMaxIfGreater(other.getMax());
        updateMinIfLesser(other.getMin());
        updateCount(other.getCount());

        return this;
    }

    /**
     * Calculates the average with the sum and count in this bucket.
     */
    double getAvg() {
        return this.sum / this.count;
    }

    /**
     * Increments the sum in this bucket with the amount.
     */
    private void updateSum(double amount) {
        this.sum += amount;
    }

    /**
     * Updates the max value for this bucket when the amount is greater.
     */
    private void updateMaxIfGreater(double amount) {
        if (max < amount) {
            this.max = amount;
        }
    }

    /**
     * Updates the min value for this bucket when the amount is lesser.
     */
    private void updateMinIfLesser(double amount) {
        if (min > amount) {
            this.min = amount;
        }
    }

    /**
     * Increments the count in this bucket with the count informed.
     */
    private void updateCount(long count) {
        this.count += count;
    }

    /**
     * Merger method for two buckets.
     */
    static StatisticBucket merger(StatisticBucket b1, StatisticBucket b2) {

        if (b1 != null) {
            return b1.merge(b2);
        } else {
            return b2;
        }
    }
}
