package com.ensa.SprintFlow.exception.generalException;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import com.ensa.SprintFlow.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class InvalidDataException extends ApplicationException {

    public InvalidDataException(String message) {
        super(message);
    }

    @Override
    public ResponseEntity<?> getErrorResponse() {
        return new ErrorResponseBuilder()
                .error("INVALID_DATA")
                .status( HttpStatus.BAD_REQUEST)
                .message( getMessage())
                .build();
    }
}
