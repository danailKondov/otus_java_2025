package ru.otus;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    private static final int START_VALUE = 1;
    private static final int END_VALUE = 10;
    private static final int THREADS_NUMBER = 2;

    public static void main(String[] args) {
        CountPrinter countPrinter = new CountPrinter(START_VALUE, END_VALUE);
        try (ExecutorService executorService = getExecutorService()) {
            executorService.submit(() -> countPrinter.countAndPrint("first"));
            executorService.submit(() -> countPrinter.countAndPrint("second"));
        }
    }

    private static ExecutorService getExecutorService() {
        return Executors.newFixedThreadPool(
                THREADS_NUMBER,
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .build());
    }
}
