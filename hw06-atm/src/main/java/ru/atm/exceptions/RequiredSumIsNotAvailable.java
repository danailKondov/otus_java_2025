package ru.atm.exceptions;

public class RequiredSumIsNotAvailable extends RuntimeException {
    public RequiredSumIsNotAvailable(String message) {
        super(message);
    }
}
