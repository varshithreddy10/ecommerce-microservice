package com.ecom.adminapi.exception;

public class GenericApiException extends RuntimeException
{
    GenericApiException(String message)
    {
        super(message);
    }
}
