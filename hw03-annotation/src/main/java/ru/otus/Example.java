package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class Example {

    @Before
    public void setup() {
        System.out.println("before test");
    }

    @Test
    public void firstTestedMethod() {
        System.out.println("first test processed");
    }

    @Test
    public void secondTestedMethod() {
        throw new RuntimeException("second test failed");
    }

    @After
    public void close() {
        System.out.println("after test");
    }
}
