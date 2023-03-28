package edu.eci.proyect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExistException extends ResponseStatusException {
    public UserAlreadyExistException(String userEmail) {
        super(HttpStatus.CONFLICT, "User" + userEmail + " is already Created");
    }
}
