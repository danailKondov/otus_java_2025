package ru.otus.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@Table(name = "client")
public class Client {

    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id", keyColumn = "client_id")
    private List<Phone> phones;

    public Client(String name) {
        this(null, name, null, null);
    }

    public Client() {
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }
}
