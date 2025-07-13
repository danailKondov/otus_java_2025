package ru.otus.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "phone")
public record Phone(
        @Id Long id,
        Long clientId,
        String number) {
}
