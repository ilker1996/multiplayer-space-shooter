package com.swconstruction.backend.User;

import com.swconstruction.backend.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.persistence.EntityExistsException;

@ControllerAdvice
public class UserAlreadyExistsHandler extends EntityExistsException {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> userExistsHandler(UserAlreadyExistsException ex) {
        Error error = new Error(HttpStatus.CONFLICT);
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());
    }
}
