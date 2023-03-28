package edu.eci.proyect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidUserProvidedException extends ResponseStatusException {
    public InvalidUserProvidedException()
    {
        super(HttpStatus.BAD_REQUEST, "The user can't be null");
    }
}
