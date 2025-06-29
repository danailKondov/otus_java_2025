package ru.otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.ClientCreateDto;
import ru.otus.dto.ClientViewDto;
import ru.otus.entity.Address;
import ru.otus.entity.Client;
import ru.otus.entity.Phone;
import ru.otus.repository.ClientsRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientsService {

    private final ClientsRepository clientsRepository;

    public List<Client> findAllClients() {
        return clientsRepository.findAll();
    }

    public ClientViewDto mapToDto(Client client) {
        return new ClientViewDto(
                client.id(),
                client.name(),
                client.address() != null ? client.address().street() : null,
                client.phones().stream()
                        .map(Phone::number)
                        .toList());
    }

    @Transactional
    public void createClient(ClientCreateDto formData) {
        Set<Phone> phones = Arrays.stream(formData.getPhoneNumbers().split(","))
                .map(num -> new Phone(null, null, num))
                .collect(Collectors.toSet());
        Client client = clientsRepository.save(new Client(
                null,
                formData.getName(),
                new Address(null, formData.getAddress()),
                phones));
        log.info("Client created: {}", client);
    }
}
