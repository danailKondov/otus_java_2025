package ru.otus.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "address")
public record Address(
        @Id Long clientId,
        String street) {
}
