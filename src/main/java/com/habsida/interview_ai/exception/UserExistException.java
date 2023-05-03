package com.habsida.interview_ai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserExistException extends RuntimeException{
    public UserExistException(String message) {
        super(message);
    }
}
