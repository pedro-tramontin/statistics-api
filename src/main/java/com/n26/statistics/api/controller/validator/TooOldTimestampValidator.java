package com.n26.statistics.api.controller.validator;

import com.n26.statistics.api.controller.validator.annotation.NotTooOld;

import java.time.Instant;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TooOldTimestampValidator implements ConstraintValidator<NotTooOld, Instant> {

    @Value("${app.transaction.timestamp.min-past-interval}")
    private int maxInterval;

    @Override
    public void initialize(NotTooOld notTooOld) {
        if (maxInterval == 0) {
            maxInterval = notTooOld.maxInterval();
        }
    }

    @Override
    public boolean isValid(final Instant timestamp, ConstraintValidatorContext context) {

        Instant minTime = Instant.now().minusSeconds(maxInterval);

        return minTime.isBefore(timestamp);
    }
}
