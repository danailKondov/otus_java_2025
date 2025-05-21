package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;

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

    private DbServiceClientImpl dbServiceClient;

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
        DriverManagerDataSource dataSource = new DriverManagerDataSource(postgresqlContainer.getJdbcUrl(), USER, PASSWORD);
        TransactionRunnerJdbc transactionRunner = new TransactionRunnerJdbc(dataSource);
        DbExecutorImpl dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        DataTemplateJdbc<Client> dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);
        dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
    }

    @Test
    @DisplayName("Тест вставки и поиска всех записей")
    void insertAndFindAllTest() {
        // setup
        dbServiceClient.saveClient(new Client("name"));
        dbServiceClient.saveClient(new Client("name2"));
        dbServiceClient.saveClient(new Client("name3"));

        // when
        List<Client> result = dbServiceClient.findAll();

        // then
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Поиск записей по id")
    void findById() {
        // setup
        dbServiceClient.saveClient(new Client("name"));
        dbServiceClient.saveClient(new Client("name2"));
        Client savedClient = dbServiceClient.saveClient(new Client("name3"));

        // when
        Optional<Client> optional = dbServiceClient.getClient(savedClient.getId());

        // then
        assertTrue(optional.isPresent());
        assertEquals("name3", optional.get().getName());
    }

    @Test
    @DisplayName("Обновление")
    void update() {
        // setup
        Client client = dbServiceClient.saveClient(new Client("name3"));
        Optional<Client> optional = dbServiceClient.getClient(client.getId());

        // when
        assertTrue(optional.isPresent());
        Client savedClient = optional.get();
        savedClient.setName(TEST_NAME);
        dbServiceClient.saveClient(savedClient); // update

        // then
        List<Client> result = dbServiceClient.findAll();
        assertEquals(1, result.size());
        Client first = result.getFirst();
        assertEquals(first.getName(), TEST_NAME);
    }
}