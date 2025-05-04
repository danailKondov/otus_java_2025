package ru.otus.dataprocessor;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);

    private final ObjectMapper mapper;
    private final JsonParser parser;

    public ResourcesFileLoader(String fileName) {
        try (InputStream is = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            this.mapper = JsonMapper.builder().build();
            mapper.registerModule(new JavaTimeModule());
            this.parser = mapper.createParser(is);
        } catch (Exception e) {
            logger.error("Ошибка при создании ResourcesFileLoader", e);
            throw new FileProcessException(e);
        }
    }

    @Override
    @SneakyThrows
    public List<Measurement> load() {
        return mapper.readValue(parser, new TypeReference<>() {});
    }
}
