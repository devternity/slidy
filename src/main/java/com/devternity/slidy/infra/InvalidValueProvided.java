package com.devternity.slidy.infra;

public class InvalidValueProvided extends RuntimeException {

    public InvalidValueProvided(String item) {
        super(item + " is not valid");
    }
}
