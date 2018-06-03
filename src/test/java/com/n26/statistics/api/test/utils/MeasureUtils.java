package com.n26.statistics.api.test.utils;

import java.util.Arrays;
import java.util.function.Supplier;

public class MeasureUtils {

    public static long measureXTimesExecution(int times, Supplier<Long> supplier) {

        long measures[] = new long[times];
        for (int i = 0; i < times; i++) {
            measures[i] = supplier.get();
        }

        return Arrays.stream(measures).sum() / times;
    }
}
