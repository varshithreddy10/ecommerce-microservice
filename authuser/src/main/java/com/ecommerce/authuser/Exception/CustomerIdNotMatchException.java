package com.ecommerce.authuser.Exception;

public class CustomerIdNotMatchException extends  RuntimeException
{
    private String message;
    public CustomerIdNotMatchException(String message)
    {
        super(message);
        this.message=message;
    }
}
