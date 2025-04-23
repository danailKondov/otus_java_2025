package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;


public class ProcessorEvenSecondChecker implements Processor {

    private TimeProvider timeProvider;

    public ProcessorEvenSecondChecker(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        int second = timeProvider.getTime().getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException("Even second exception");
        }
        return message;
    }
}
