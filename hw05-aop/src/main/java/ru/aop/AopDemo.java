package ru.aop;

public class AopDemo {

    public static void main(String[] args) {

        TestLogging testLogging = IoC.createProxyByClass(TestLoggingImpl.class);

        testLogging.calculation(1);
        testLogging.calculation(3, 1);
        testLogging.calculation(3, 1, "FIVE");
    }
}
