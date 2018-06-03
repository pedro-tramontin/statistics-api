package com.n26.statistics.api.controller.exception.handler;

import com.n26.statistics.api.controller.validator.annotation.NotTooOld;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handler for all the exceptions
 */
@ControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {

            if (NotTooOld.class.getSimpleName().equals(fieldError.getCode())) {
                LOGGER.warn(messageSource.getMessage("error.message.timestamp-too-old", null, Locale
                    .getDefault()), fieldError.getRejectedValue());

                return handleExceptionInternal(ex, null, headers, HttpStatus.NO_CONTENT, request);
            }
        }

        return handleExceptionInternal(ex, null, headers, status, request);
    }

}
