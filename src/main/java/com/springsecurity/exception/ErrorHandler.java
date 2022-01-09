package com.springsecurity.exception;

import java.util.HashMap;
import java.util.Map;

import com.springsecurity.constant.ErrorCode;
import com.springsecurity.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.springsecurity.util.MessageResolverUtil.resolve;


@Slf4j
@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("Error method argument not valid, message: {}", ex.getMessage());
        Map<String, String> warnings = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
            warnings.put(fieldError.getField(), resolve(fieldError.getDefaultMessage()));

        return new ResponseEntity<>(new ErrorDto(warnings), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegisterAlreadyExistException.class)
    public ResponseEntity<ErrorDto> registerAlreadyExistException(RegisterAlreadyExistException ex) {
        log.error("Response message: {}", ex.getMessage());
        ErrorDto response = new ErrorDto(ErrorCode.REGISTER_ALREADY_EXIST);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidUserCredentialException.class)
    public ResponseEntity<ErrorDto> invalidUserCredentialException(InvalidUserCredentialException ex) {
        log.error("Response message: {}", ex.getMessage());
        ErrorDto response = new ErrorDto(ErrorCode.INVALID_EMAIL_PASSWORD);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorDto> invalidTokenException(InvalidTokenException ex) {
        log.error("Response message: {}", ex.getMessage());
        ErrorDto response = new ErrorDto(ErrorCode.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> exception(Exception ex) {
        log.error("Response message: {}", ex.getMessage());
        ErrorDto response = new ErrorDto(ErrorCode.SERVICE_UNAVAILABLE);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
