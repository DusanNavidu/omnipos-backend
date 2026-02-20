package com.omnipos.omniposbackend.exception;

/**
 * @author Dusan
 * @date 2/19/2026
 */

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}