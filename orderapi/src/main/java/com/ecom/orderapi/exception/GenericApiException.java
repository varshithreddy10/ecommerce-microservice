package com.ecom.orderapi.exception;

public class GenericApiException extends RuntimeException
{
    public GenericApiException(String message)
    {
        super(message);
    }
}
