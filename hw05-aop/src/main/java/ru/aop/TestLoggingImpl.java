package ru.aop;

public class TestLoggingImpl implements TestLogging {

    public TestLoggingImpl() {
    }

    @Log
    @Override
    public int calculation(int param) {
        return param + 5;
    }

    @Override
    public int calculation(int param1, int param2) {
        return param1 + param2;
    }

    @Override
    public int calculation(int param1, int param2, String param3) {
        return param1 + param2 + ("FIVE".equals(param3) ? 5 : 0);
    }
}
