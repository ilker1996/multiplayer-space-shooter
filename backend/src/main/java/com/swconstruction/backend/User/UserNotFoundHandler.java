package com.swconstruction.backend.User;

import com.swconstruction.backend.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
class UserNotFoundHandler extends EntityNotFoundException {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserExists(UserNotFoundException ex) {
        Error error = new Error(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());
    }
}