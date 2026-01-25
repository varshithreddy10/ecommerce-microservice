package com.ecom.adminapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> myResourceNotFoundException(ResourceNotFoundException e)
    {
        String message = e.getMessage();
        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericApiException.class)
    public ResponseEntity<String> myGenericApiException(GenericApiException e)
    {
        String message = e.getMessage();
        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompileTimeException.class)
    public ResponseEntity<String> myCompileTimeException(CompileTimeException e)
    {
        String message = e.getMessage();
        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> myValidationError(MethodArgumentNotValidException e)
    {
        String message = e.getMessage();
        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> myException(Exception e)
    {
        String message = e.getMessage();
        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }
}
