package com.swconstruction.backend.User;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String message)
    {
        super(message);
    }

}
