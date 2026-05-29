package ru.nursafin.exception;

public class RateUnavailableException extends RuntimeException {
    public RateUnavailableException(String message) {
        super(message);
    }
}
