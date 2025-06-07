package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final String tableName;
    private final String idFieldName;
    private final List<Field> fieldsWithoutId;


    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.tableName = entityClassMetaData.getName();
        this.idFieldName = entityClassMetaData.getIdField().getName();
        this.fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", tableName);
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select * from %s where %s = ?", tableName, idFieldName);
    }

    @Override
    public String getInsertSql() {
        return String.format("insert into %s (%s) values(%s);",
                tableName,
                getFieldsWithoutIdString(),
                getInsertTemplate());
    }

    @Override
    public String getUpdateSql() {
        return String.format("update %s set %s where %s = ?;",
                tableName,
                getUpdateTemplate(),
                idFieldName);
    }

    private String getUpdateTemplate() {
        return fieldsWithoutId.stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
    }

    private String getInsertTemplate() {
        return fieldsWithoutId.stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));
    }

    private String getFieldsWithoutIdString() {
        return fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
    }
}
