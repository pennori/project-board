package com.board.api.global.handler;

import com.board.api.domain.member.exception.DuplicateException;
import com.board.api.global.dto.response.ApiResponse;
import com.board.api.global.dto.ErrorMessage;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(
                ApiResponse.<Map<String, String>>builder()
                        .resultCode(HttpStatus.BAD_REQUEST.value())
                        .resultMessage(HttpStatus.BAD_REQUEST.name())
                        .data(errors)
                        .build()
        );
    }

    @ExceptionHandler(value = {FeignException.class})
    public ResponseEntity<ApiResponse<ErrorMessage>> feignExceptionHandler(final FeignException ex) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.<ErrorMessage>builder()
                        .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .resultMessage(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(value = {DuplicateException.class})
    public ResponseEntity<ApiResponse<ErrorMessage>> duplicateExceptionHandler(final DuplicateException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.<ErrorMessage>builder()
                        .resultCode(HttpStatus.BAD_REQUEST.value())
                        .resultMessage(HttpStatus.BAD_REQUEST.name())
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ApiResponse<ErrorMessage>> runtimeExceptionHandler(final RuntimeException ex) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.<ErrorMessage>builder()
                        .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .resultMessage(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> illegalArgumentExceptionHandler(final IllegalArgumentException ex) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.<ErrorMessage>builder()
                        .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .resultMessage(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<Object> illegalStateExceptionHandler(final IllegalStateException ex) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.<ErrorMessage>builder()
                        .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .resultMessage(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }
}
