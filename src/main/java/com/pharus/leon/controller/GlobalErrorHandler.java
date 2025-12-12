package com.pharus.leon.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);
    public static final String DB_TEMPORARILY_UNAVAILABLE = "The database is temporarily unavailable";
    public static final String APP_TEMPORARILY_UNAVAILABLE = "The application is temporarily unavailable";

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> globalException(CannotGetJdbcConnectionException ex) {
        logger.warn(DB_TEMPORARILY_UNAVAILABLE, ex);

        ProblemDetail ex500 = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE, DB_TEMPORARILY_UNAVAILABLE
        );
        return ResponseEntity.internalServerError().body(ex500);
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> globalException(Exception ex) {
        logger.error(APP_TEMPORARILY_UNAVAILABLE, ex);

        ProblemDetail ex500 = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, APP_TEMPORARILY_UNAVAILABLE
        );
        return ResponseEntity.internalServerError().body(ex500);
    }

}
