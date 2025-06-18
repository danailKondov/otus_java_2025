package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.EquationPreparer;
import ru.otus.services.EquationPreparerImpl;
import ru.otus.services.GameProcessor;
import ru.otus.services.GameProcessorImpl;
import ru.otus.services.IOService;
import ru.otus.services.IOServiceStreams;
import ru.otus.services.PlayerService;
import ru.otus.services.PlayerServiceImpl;

//@AppComponentsContainerConfig(order = 2)
public class AppConfig2 {

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(
            IOService ioService, PlayerService playerService, EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}
