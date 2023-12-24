package com.javalab.boot.exception;

public class OutOfMoneyException extends RuntimeException{
    public OutOfMoneyException(String message){
        super(message);
    }
}
