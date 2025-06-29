package ru.otus.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.entity.Address;

public interface AddressRepository extends ListCrudRepository<Address, Long> {
}
