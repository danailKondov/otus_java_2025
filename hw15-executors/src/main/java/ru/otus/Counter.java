package ru.otus;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Counter {

    private int count;
    private boolean reverseOrder = false;
    private final int endValue;

    public Counter(int startValue, int endValue) {
        count = startValue;
        this.endValue = endValue;
    }

    public void changeValue() {
        if (!reverseOrder) {
            count++;
            if (count >= endValue) {
                reverseOrder = true;
            }
        } else {
            count--;
        }
    }
}
