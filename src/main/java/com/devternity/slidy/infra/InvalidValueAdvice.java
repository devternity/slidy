package com.devternity.slidy.infra;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class InvalidValueAdvice {

    @ExceptionHandler(value = {InvalidValueProvided.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleValidationFailure(InvalidValueProvided ex) {
        return ex.getLocalizedMessage();
    }

}
