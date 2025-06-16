package ru.otus.exceptions;

public class BeanAlreadyExistsException extends RuntimeException {
    public BeanAlreadyExistsException(String message) {
        super(message);
    }
}
