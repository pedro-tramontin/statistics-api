package com.n26.statistics.api.controller.validator.annotation;

import com.n26.statistics.api.controller.validator.TooOldTimestampValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation that checks if bucketAtInstantLessSecond Instant value is too old.
 *
 * It is based on the property app.transaction.timestamp.min-past-interval or the value in
 * maxInterval
 */
@Constraint(validatedBy = TooOldTimestampValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface NotTooOld {

    int maxInterval() default 60;

    String message() default "Timestamp is too old.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
