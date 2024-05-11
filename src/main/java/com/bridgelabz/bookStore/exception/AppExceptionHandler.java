package com.bridgelabz.bookStore.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value={BookStoreException.class})
    public final ResponseEntity<ErrorMessage> handleBookStoreException(Exception e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        request.getDescription(false);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value={ConstraintViolationException.class})
    public final ResponseEntity<ErrorMessage> handleConstraintViolationException(Exception e, WebRequest request) {
        e.getMessage();
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        request.getDescription(false);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
