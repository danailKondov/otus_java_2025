package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Tester {

    private Method beforeMethod;
    private Method afterMethod;
    private final Map<String, TestResult> result = new HashMap<>();

    public static <T> void processTestsForClass(Class<T> tClass) {
        new Tester().test(tClass);
    }

    private <T> void test(Class<T> tClass) {
        try {
            initiateBeforeAndAfterMethods(tClass);
            processTests(tClass);
            printResult();
        } catch (Exception e) {
            System.out.println("Tests failed:");
            e.printStackTrace();
        }
    }

    private <T> void processTests(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (Method method : tClass.getMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                runTestMethod(method, getInstance(tClass));
            }
        }
    }

    private static <T> T getInstance(Class<T> tClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<T> constructor = tClass.getConstructor();
        return constructor.newInstance();
    }

    private void printResult() {
        System.out.println();
        System.out.println("TEST RESULTS:");
        System.out.println();
        result.forEach((testName, result) -> {

            if (result.getPassed()) {
                System.out.printf("Test \"%s\" PASSED", testName);
                System.out.println();
            } else {
                System.out.printf("Test \"%s\" FAILED: %s", testName, result.getException());
                System.out.println();
            }
        });
    }

    private <T> void runTestMethod(Method method, T instance) throws IllegalAccessException, InvocationTargetException {
        beforeMethod.invoke(instance);
        try {
            method.invoke(instance);
            result.put(method.getName(), new TestResult(true));
        } catch (InvocationTargetException e) {
            result.put(method.getName(), new TestResult(false, e.getCause()));
        } finally {
            afterMethod.invoke(instance);
        }
    }

    private <T> void initiateBeforeAndAfterMethods(Class<T> tClass) {
        for (Method method : tClass.getMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethod = method;
            }
            if (method.isAnnotationPresent(After.class)) {
                afterMethod = method;
            }
        }
    }

    private static class TestResult {
        private final boolean isPassed;
        private final Throwable exception;

        public TestResult(boolean isPassed, Throwable exception) {
            this.isPassed = isPassed;
            this.exception = exception;
        }

        public TestResult(boolean isPassed) {
            this.isPassed = isPassed;
            this.exception = null;
        }

        public boolean getPassed() {
            return isPassed;
        }

        public Throwable getException() {
            return exception;
        }
    }
}