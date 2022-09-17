package com.swconstruction.backend.User;

import javax.persistence.EntityNotFoundException;

public class BadRequestException extends EntityNotFoundException
{

    public BadRequestException(String message)
    {
        super(message);
    }

}
