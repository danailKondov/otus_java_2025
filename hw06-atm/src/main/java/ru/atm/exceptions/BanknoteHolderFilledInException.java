package ru.atm.exceptions;

public class BanknoteHolderFilledInException extends RuntimeException {
    public BanknoteHolderFilledInException(String message) {
        super(message);
    }
}
