package com.osttra.alpine.exceptions;

public class ProcessNotFoundException extends RuntimeException{
    public ProcessNotFoundException(String message) {
        super(message);
    }
}
