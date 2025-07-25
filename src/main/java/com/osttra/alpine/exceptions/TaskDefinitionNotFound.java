package com.osttra.alpine.exceptions;

public class TaskDefinitionNotFound extends RuntimeException{
    public TaskDefinitionNotFound(String message) {
        super(message);
    }
}
