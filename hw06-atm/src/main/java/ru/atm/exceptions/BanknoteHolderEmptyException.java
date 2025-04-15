package ru.atm.exceptions;

public class BanknoteHolderEmptyException extends RuntimeException {
    public BanknoteHolderEmptyException(String message) {
        super(message);
    }
}
