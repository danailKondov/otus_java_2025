package ru.otus.exceptions;

public class BeanDuplicateException extends RuntimeException {
    public BeanDuplicateException(String message) {
        super(message);
    }
}
