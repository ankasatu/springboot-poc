package com.tofu.demo.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends ValidationException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
