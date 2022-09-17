package com.swconstruction.backend.User;

import javax.persistence.EntityExistsException;

public class UserAlreadyExistsException extends EntityExistsException {
    public UserAlreadyExistsException(String message)
    {
        super(message);
    }
}
