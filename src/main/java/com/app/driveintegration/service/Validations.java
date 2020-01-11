package com.app.driveintegration.service;

import com.app.driveintegration.exception.InvalidArgumentException;

public class Validations {

    public void validatePath(String path) throws InvalidArgumentException {
        if (!path.startsWith("/")) {
            throw new InvalidArgumentException("path must start with prefix as /");
        }

        //TODO apply more validations
    }
}
