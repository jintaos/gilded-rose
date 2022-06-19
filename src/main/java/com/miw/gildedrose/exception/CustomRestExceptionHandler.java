package com.miw.gildedrose.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST,request);
    }

    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.EXPECTATION_FAILED,request);
    }

    private ResponseEntity<ApiError> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError.ApiErrorBuilder()
                .status(status)
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        log.error("An exception has occurred. ApiError: {}", apiError);
        return new ResponseEntity<>(apiError, status);
    }

}
