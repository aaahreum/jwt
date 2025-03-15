package com.example.jwt.global.config.exception;

import com.example.jwt.global.enums.ErrorCode;
import com.example.jwt.user.dto.ErrorResponseDto;
import com.example.jwt.user.dto.ErrorResponseWrapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;
/**
 * 전역에서 예외를 처리하는 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<ErrorResponseWrapper> handleMethodValidationExceptions(
            HandlerMethodValidationException e) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INVALID_CREDENTIALS);
        ErrorResponseWrapper responseWrapper = new ErrorResponseWrapper(errorResponseDto);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseWrapper> handleValidationExceptions(
            MethodArgumentNotValidException e) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INVALID_CREDENTIALS);
        ErrorResponseWrapper responseWrapper = new ErrorResponseWrapper(errorResponseDto);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseWrapper> handleIllegalArgumentExceptions(
            IllegalArgumentException e) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INVALID_CREDENTIALS);
        ErrorResponseWrapper responseWrapper = new ErrorResponseWrapper(errorResponseDto);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ErrorResponseWrapper> handleResponseStatusExceptions(
            ResponseStatusException e) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.USER_ALREADY_EXISTS);
        ErrorResponseWrapper responseWrapper = new ErrorResponseWrapper(errorResponseDto);

        return ResponseEntity.status(e.getStatusCode()).body(responseWrapper);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponseWrapper> handleAuthException(
            AuthenticationException e) {
        HttpStatus statusCode = e instanceof BadCredentialsException
                ? HttpStatus.FORBIDDEN
                : HttpStatus.UNAUTHORIZED;

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INVALID_CREDENTIALS);
        ErrorResponseWrapper responseWrapper = new ErrorResponseWrapper(errorResponseDto);

        return ResponseEntity.status(statusCode).body(responseWrapper);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponseWrapper> handleAccessDeniedException(AccessDeniedException e) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.ACCESS_DENIED);
        ErrorResponseWrapper responseWrapper = new ErrorResponseWrapper(errorResponseDto);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseWrapper);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<ErrorResponseWrapper> handleAuthorizationDeniedException(AuthorizationDeniedException e) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.ACCESS_DENIED);
        ErrorResponseWrapper responseWrapper = new ErrorResponseWrapper(errorResponseDto);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseWrapper);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponseWrapper> handleJwtException(JwtException e) {
        HttpStatus httpStatus = e instanceof ExpiredJwtException ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INVALID_TOKEN);
        ErrorResponseWrapper responseWrapper = new ErrorResponseWrapper(errorResponseDto);

        return ResponseEntity.status(httpStatus).body(responseWrapper);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseWrapper> handleOtherExceptions(Exception e) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INTERNAL_SERVER_ERROR);
        ErrorResponseWrapper responseWrapper = new ErrorResponseWrapper(errorResponseDto);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseWrapper);
    }
}

