package edu.eci.proyect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalArgumentOfUserException extends ResponseStatusException {
    public IllegalArgumentOfUserException(){
        super(HttpStatus.BAD_REQUEST, "arguments from method are incorrect");
    }
}
