package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorEvenSecondCheckerTest {

    @Test
    @DisplayName("Тестируем обработку в четную секунду")
    void handleExceptionTest() {
        var message = new Message.Builder(1L).field6("test").build();
        var processor = new ProcessorEvenSecondChecker(() -> LocalTime.of(0, 0, 22));
        assertThrows(RuntimeException.class, () -> processor.process(message));
    }

    @Test
    @DisplayName("Тестируем обработку в нечетную секунду")
    void handleNoExceptionTest() {
        var message = new Message.Builder(1L).field7("test").build();
        var processor = new ProcessorEvenSecondChecker(() -> LocalTime.of(0, 0, 23));
        Message result = processor.process(message);
        assertNotNull(result);
    }
}