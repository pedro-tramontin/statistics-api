package com.n26.statistics.api.test.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class FakeData {

    /**
     * Generates a random double between 5000 and -5000
     */
    public static double randomAmount() {
        return randomAmount(-5000.0, 5000.0);
    }

    /**
     * Generates a bounded double between min and max values and round to the 2nd decimal
     */
    public static double randomAmount(double min, double max) {
        return BigDecimal.valueOf(Math.random() * (max - min) - max).setScale(2, RoundingMode
            .HALF_UP).doubleValue();
    }

    public static Instant randomTime(Instant from, Instant to) {

        long fromEpoch = from.toEpochMilli();
        long toEpoch = to.toEpochMilli();

        long random = (long) (Math.random() * (toEpoch - fromEpoch));

        return Instant.ofEpochMilli(fromEpoch + random);
    }

}
