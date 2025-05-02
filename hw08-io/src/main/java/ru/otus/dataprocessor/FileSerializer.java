package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

public class FileSerializer implements Serializer {

    private static final Logger logger = LoggerFactory.getLogger(FileSerializer.class);

    private final ObjectMapper mapper;
    private final File file;

    public FileSerializer(String fileName) {
        logger.info("result filename: {}", fileName);
        this.file = new File(fileName);
        this.mapper = JsonMapper.builder().build();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    @SneakyThrows
    public void serialize(Map<String, Double> data) {
        mapper.writeValue(file, data);
    }
}
