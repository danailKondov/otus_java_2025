package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class CachedDbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(CachedDbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<Long, Client> clientCache;

    public CachedDbServiceClientImpl(TransactionRunner transactionRunner,
                                     DataTemplate<Client> dataTemplate,
                                     HwCache<Long, Client> clientCache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.clientCache = clientCache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                clientCache.put(createdClient.getId(), createdClient);
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            clientCache.put(client.getId(), client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Optional<Client> client = Optional.ofNullable(clientCache.get(id));
        if (client.isPresent()) {
            return client;
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            clientOptional.ifPresent(value -> clientCache.put(value.getId(), value));
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            clientList.forEach(client -> clientCache.put(client.getId(), client));
            return clientList;
        });
    }
}
