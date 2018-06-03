package com.n26.statistics.api.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * Utilitary class
 */
@Component
public class Utils {

    /**
     * Zero BigDecimal scaled to 2 decimal.
     */
    public static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);

    /**
     * Call to Instant.now().
     *
     * Used to permit better testing of classes that needs now().
     */
    public Instant now() {
        return Instant.now();
    }

    /**
     * Generates the key for the buckets Map based on the Instant.
     *
     * It basically returns the epochSecond for the instant object, since each bucket in the map
     * represents all the transactions of that second.
     */
    public long mapKey(Instant instant) {
        return instant.getEpochSecond();
    }

    /**
     * Transforms bucketAtInstantLessSecond double value to BigDecimal and scales it to 2 decimals.
     */
    public BigDecimal toBigDecimalScaledToTwo(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

}
