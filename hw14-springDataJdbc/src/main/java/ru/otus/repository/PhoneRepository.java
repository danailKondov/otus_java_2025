package ru.otus.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.entity.Phone;

public interface PhoneRepository extends ListCrudRepository<Phone, Long> {

}
