package ru.otus.jdbc.mapper;



import lombok.RequiredArgsConstructor;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.exception.ReflectionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/** Сохраняет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
@RequiredArgsConstructor
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                singleResultHandler());
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                        connection,
                        entitySQLMetaData.getSelectAllSql(),
                        Collections.emptyList(),
                        multipleResultHandler())
                .orElse(new ArrayList<>());
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getInsertSql(),
                getValues(client));
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> values = new ArrayList<>(getValues(client));
        values.add(getValueFromField(client, entityClassMetaData.getIdField()));
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), values);
    }

    private Function<ResultSet, T> singleResultHandler() {
        return rs -> {
            try {
                if (rs.next()) {
                    return mapRsToObject(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        };
    }

    private Function<ResultSet, ArrayList<T>> multipleResultHandler() {
        return rs -> {
            var list = new ArrayList<T>();
            try {
                while (rs.next()) {
                    list.add(mapRsToObject(rs));
                }
                return list;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        };
    }

    private T mapRsToObject(ResultSet rs) {
        try {
            Constructor<T> constructor = entityClassMetaData.getConstructor();
            T object = constructor.newInstance();
            List<Field> allFields = entityClassMetaData.getAllFields();
            for (Field field : allFields) {
                field.setAccessible(true);
                field.set(object, rs.getObject(field.getName()));
            }
            return object;
        } catch (SQLException e) {
            throw new DataTemplateException(e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    private List<Object> getValues(T client) {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> getValueFromField(client, field))
                .toList();
    }

    private Object getValueFromField(T object, Field field) {
        try {
            boolean isAccessible = field.canAccess(object);
            field.setAccessible(true);
            Object result = field.get(object);
            field.setAccessible(isAccessible);
            return result;
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }
}
