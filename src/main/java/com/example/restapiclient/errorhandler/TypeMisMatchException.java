package com.example.restapiclient.errorhandler;

public class TypeMisMatchException extends RuntimeException{
    public TypeMisMatchException(String message) {
        super(message);
    }
}
