package com.eMartix.auth_service.exception;

import com.eMartix.auth_service.dto.response.ErrorResponseDto;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponseDto response = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .error("Validation Error")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtErrors(JwtException ex) {
        log.info("JWT Exception"+ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .error("JWT Error")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllOtherErrors(Exception ex) {
        ErrorResponseDto response =
                  ErrorResponseDto.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(ex.getMessage())
                            .error("Internal Server Error")
                            .timestamp(LocalDateTime.now())
                            .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
