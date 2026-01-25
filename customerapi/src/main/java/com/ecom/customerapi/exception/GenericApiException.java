package com.ecom.customerapi.exception;

public class GenericApiException extends RuntimeException
{
    GenericApiException(String message)
    {
        super(message);
    }
}
