package com.eMartix.commons.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.net.URI;
import java.util.*;

public interface CommonExceptionHandler {
    Logger log  = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default ProblemDetail accessDeniedExceptionHandler(AccessDeniedException e) {
        return ProblemDetailsBuilder.statusAndDetail(HttpStatus.FORBIDDEN, e.getMessage())
                .type(URI.create("https://problems.anhTuan.com/access-denied"))
                .title("Access Denied")
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default ProblemDetail authenticationExceptionHandler(AuthenticationException e) {
        return ProblemDetailsBuilder.statusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage())
                .type(URI.create("https://problems.anhTuan.com/authentication-error"))
                .title("Authentication Error")
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default ProblemDetail resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        return ProblemDetailsBuilder.statusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage())
                .type(URI.create("https://problems.anhTuan.com/resource-not-found"))
                .title("Resource Not Found")
                .build();
    }


}
