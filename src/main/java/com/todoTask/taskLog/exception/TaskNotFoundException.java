package com.todoTask.taskLog.exception;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(String exception){
        super(exception);
    }
}
