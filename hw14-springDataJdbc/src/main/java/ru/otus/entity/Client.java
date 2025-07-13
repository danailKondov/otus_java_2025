package ru.otus.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table(name = "client")
public record Client(
        @Id Long id,
        String name,
        @MappedCollection(idColumn = "client_id") Address address,
        @MappedCollection(idColumn = "client_id") Set<Phone> phones) {
}
