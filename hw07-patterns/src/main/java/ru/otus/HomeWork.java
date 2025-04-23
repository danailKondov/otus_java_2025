package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.homework.ProcessorEvenSecondChecker;
import ru.otus.processor.homework.ProcessorField11Field12Exchange;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {

    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var processors = List.of(
                new ProcessorField11Field12Exchange(),
                new LoggerProcessor((new ProcessorEvenSecondChecker(LocalTime::now))));

        var complexProcessor = new ComplexProcessor(processors, ex -> logger.error("Ошибка при обработке", ex));

        var listenerPrinter = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        List<String> listField13 = new ArrayList<>();
        listField13.add("someText");
        listField13.add("someOtherText");

        ObjectForMessage objField13 = new ObjectForMessage();
        objField13.setData(listField13);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field11("field11")
                .field12("field12")
                .field13(objField13)
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result: {}", result);

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(historyListener);
    }
}
