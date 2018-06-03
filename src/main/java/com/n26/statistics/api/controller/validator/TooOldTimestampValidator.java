package com.n26.statistics.api.controller.validator;

import com.n26.statistics.api.controller.validator.annotation.NotTooOld;

import java.time.Instant;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Validator for the annotation {@link NotTooOld} to check if bucketAtInstantLessSecond Instant is too old.
 *
 * It checks if the Instant is older for bucketAtInstantLessSecond period that is indicated in the app.transaction
 * .timestamp.min-past-interval property or in the annotation.minPastInterval value.
 */
@Component
public class TooOldTimestampValidator implements ConstraintValidator<NotTooOld, Instant> {

    @Value("${app.transaction.timestamp.min-past-interval}")
    private int minPastInterval;

    @Override
    public void initialize(NotTooOld notTooOld) {
        if (minPastInterval == 0) {
            minPastInterval = notTooOld.maxInterval();
        }
    }

    @Override
    public boolean isValid(final Instant timestamp, ConstraintValidatorContext context) {

        Instant minTime = Instant.now().minusSeconds(minPastInterval);

        return minTime.isBefore(timestamp);
    }
}
