package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.CachedDbServiceClientImpl;
import ru.otus.crm.service.SimpleDbServiceClientImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест работы ОРМ")
@Testcontainers
class ORMTest {

    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";
    private static final String DB_NAME = "demoDB";
    private static final String TEST_NAME = "testName";
    private static final Logger log = LoggerFactory.getLogger(ORMTest.class);

    private CachedDbServiceClientImpl cachedDbServiceClient;
    private SimpleDbServiceClientImpl simpleDbServiceClient;

    @Container
    private final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName(DB_NAME)
            .withUsername(USER)
            .withPassword(PASSWORD)
            .withExposedPorts(5432)
            .withClasspathResourceMapping(
                    "V1__initial_schema.sql", "/docker-entrypoint-initdb.d/V1__initial_schema.sql", BindMode.READ_ONLY);

    @BeforeEach
    public void init() {
        var dataSource = new DriverManagerDataSource(postgresqlContainer.getJdbcUrl(), USER, PASSWORD);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();
        var entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        var entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);
        var cache = new MyCache<Long, Client>();
        cachedDbServiceClient = new CachedDbServiceClientImpl(transactionRunner, dataTemplateClient, cache);
        simpleDbServiceClient = new SimpleDbServiceClientImpl(transactionRunner, dataTemplateClient);
    }

    @Test
    @DisplayName("Поиск записей по id с кэшом и без")
    void findByIdWithCache() {
        // setup
        cachedDbServiceClient.saveClient(new Client("name"));
        cachedDbServiceClient.saveClient(new Client("name2"));
        Client savedClient = cachedDbServiceClient.saveClient(new Client("name3"));

        // when

        long start = System.nanoTime();
        Optional<Client> optional = cachedDbServiceClient.getClient(savedClient.getId());
        long end = System.nanoTime();
        long cachedSearchTimeNanos = end - start;

        start = System.nanoTime();
        Optional<Client> optional2 = simpleDbServiceClient.getClient(savedClient.getId());
        end = System.nanoTime();
        long simpleSearchTimeNanos = end - start;

        log.info("cachedSearchTimeNanos = {}", cachedSearchTimeNanos);
        log.info("simpleSearchTimeNanos = {}", simpleSearchTimeNanos);

        // then
        assertTrue(optional.isPresent());
        assertEquals("name3", optional.get().getName());
        assertTrue(optional2.isPresent());
        assertEquals("name3", optional2.get().getName());
        assertTrue(cachedSearchTimeNanos < simpleSearchTimeNanos);
    }

    @Test
    @DisplayName("Тест вставки и поиска всех записей")
    void insertAndFindAllTest() {
        // setup
        cachedDbServiceClient.saveClient(new Client("name"));
        cachedDbServiceClient.saveClient(new Client("name2"));
        cachedDbServiceClient.saveClient(new Client("name3"));

        // when
        List<Client> result = cachedDbServiceClient.findAll();

        // then
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Поиск записей по id")
    void findById() {
        // setup
        cachedDbServiceClient.saveClient(new Client("name"));
        cachedDbServiceClient.saveClient(new Client("name2"));
        Client savedClient = cachedDbServiceClient.saveClient(new Client("name3"));

        // when
        Optional<Client> optional = cachedDbServiceClient.getClient(savedClient.getId());

        // then
        assertTrue(optional.isPresent());
        assertEquals("name3", optional.get().getName());
    }

    @Test
    @DisplayName("Обновление")
    void update() {
        // setup
        Client client = cachedDbServiceClient.saveClient(new Client("name3"));
        Optional<Client> optional = cachedDbServiceClient.getClient(client.getId());

        // when
        assertTrue(optional.isPresent());
        Client savedClient = optional.get();
        savedClient.setName(TEST_NAME);
        cachedDbServiceClient.saveClient(savedClient); // update

        // then
        List<Client> result = cachedDbServiceClient.findAll();
        assertEquals(1, result.size());
        Client first = result.getFirst();
        assertEquals(first.getName(), TEST_NAME);
    }
}