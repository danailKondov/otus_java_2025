package ru.otus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountPrinter {

    private final int startValue;
    private final Counter counter;
    private final Object lock;

    private boolean isCounter = false;
    private String last;

    public CountPrinter(int startValue, int endValue) {
        this.counter = new Counter(startValue, endValue);
        this.startValue = startValue;
        this.lock = new Object();
        this.last = "first";
    }

    public void countAndPrint(String name) {
        while (isCounterInBound() && !Thread.currentThread().isInterrupted()) {
            sleep();
            synchronized (lock) {
                if (isCounterInBound()) {
                    log.info("Thread {}: value: {}", Thread.currentThread().getName(), counter.getCount());
                }
                lock.notifyAll();
                if (isCounter) {
                    counter.changeValue();
                }
                isCounter = !isCounter;
                await(name);
                last = name;
            }
        }
    }

    private boolean isCounterInBound() {
        return counter.getCount() > startValue - 1;
    }

    private void await(String name) {
        try {
            while (last.equals(name)) {
                lock.wait(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
