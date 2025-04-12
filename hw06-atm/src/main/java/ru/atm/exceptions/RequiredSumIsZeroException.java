package ru.atm.exceptions;

public class RequiredSumIsZeroException extends RuntimeException {
    public RequiredSumIsZeroException(String message) {
        super(message);
    }
}
