package com.app.pruebaApi.exception;

import java.math.BigInteger;

public class PersonNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public PersonNotFoundException(BigInteger dni) {
        super("Person not found with DNI: " + dni);
    }

    public PersonNotFoundException(String message) {
        super(message);
    }
}
