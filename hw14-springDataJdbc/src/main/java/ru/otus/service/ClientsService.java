package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.ClientCreateDto;
import ru.otus.dto.ClientViewDto;
import ru.otus.entity.Address;
import ru.otus.entity.Client;
import ru.otus.entity.Phone;
import ru.otus.repository.AddressRepository;
import ru.otus.repository.ClientsRepository;
import ru.otus.repository.PhoneRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientsService {

    private final ClientsRepository clientsRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;

    public List<Client> findAllClients() {
        return clientsRepository.findAll();
    }

    public ClientViewDto mapToDto(Client client) {
        return new ClientViewDto(
                client.getId(),
                client.getName(),
                client.getAddress() != null ? client.getAddress().getStreet() : null,
                client.getPhones().stream()
                        .map(Phone::getNumber)
                        .toList());
    }

    @Transactional
    public void createClient(ClientCreateDto formData) {
        Client client = clientsRepository.save(new Client(formData.getName()));
        savePhones(formData, client);
        addressRepository.save(new Address(formData.getAddress(), client.getId()));
    }

    private void savePhones(ClientCreateDto formData, Client client) {
        if (formData.getPhoneNumbers() != null) {
            List<Phone> phones = Arrays.stream(formData.getPhoneNumbers().split(","))
                    .map(num -> new Phone(num, client.getId()))
                    .toList();
            phoneRepository.saveAll(phones);
        }
    }
}
