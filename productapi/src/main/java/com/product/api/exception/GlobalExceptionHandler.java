package com.product.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> myMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        //String message = "code check chey bey error vachindi  MethodArgumentNotValidException ";
        String message = e.getDetailMessageCode();

        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericApiException.class)
    public ResponseEntity<String> myMethodArgumentNotValidException(GenericApiException e)
    {
        String message = e.getMessage();
        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> myResourceNotFoundException(ResourceNotFoundException e)
    {
        String message = e.getMessage();
        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> myApiGenericException(Exception e)
    {
        String message = e.getMessage();
        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }
}
