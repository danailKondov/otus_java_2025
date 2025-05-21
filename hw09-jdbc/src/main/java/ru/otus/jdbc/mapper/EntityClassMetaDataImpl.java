package ru.otus.jdbc.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.exception.IdFieldNotFoundException;
import ru.otus.jdbc.orm.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> aClass;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> aClass) {
        this.aClass = aClass;
        idField = Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(IdFieldNotFoundException::new);
        allFields = Arrays.stream(aClass.getDeclaredFields()).toList();
        fieldsWithoutId = Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }


    @Override
    public String getName() {
        return aClass.getSimpleName();
    }

    @Override
    @SneakyThrows
    public Constructor<T> getConstructor() {
        return aClass.getConstructor();
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
