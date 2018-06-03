package com.n26.statistics.api.test.utils;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Utils for measurements
 */
public class MeasureUtils {

    /**
     * Calculates the average based on the value returned from the supplier and the times it was
     * called
     */
    public static long measureXTimesExecution(int times, Supplier<Long> supplier) {

        long measures[] = new long[times];
        for (int i = 0; i < times; i++) {
            measures[i] = supplier.get();
        }

        return Arrays.stream(measures).sum() / times;
    }
}
