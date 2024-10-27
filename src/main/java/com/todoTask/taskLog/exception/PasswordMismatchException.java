package com.todoTask.taskLog.exception;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException(String exception){
        super(exception);
    }
}
