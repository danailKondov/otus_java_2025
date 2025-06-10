package ru.otus;

import com.google.gson.GsonBuilder;
import java.net.URI;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.resource.PathResourceFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.cfg.Configuration;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.persistance.core.repository.DataTemplateHibernate;
import ru.otus.persistance.core.repository.HibernateUtils;
import ru.otus.persistance.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.persistance.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.persistance.crm.model.Address;
import ru.otus.persistance.crm.model.Client;
import ru.otus.persistance.crm.model.Phone;
import ru.otus.persistance.crm.service.DbServiceClientImpl;
import ru.otus.server.ClientWebServerWithBasicSecurity;
import ru.otus.services.TemplateProcessorImpl;

/*
    Полезные для демо ссылки
    // Стартовая страница
    http://localhost:8080

    // Страница пользователей (логин/пароль user1)
    http://localhost:8080/clients

*/
public class WebServerWithBasicSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var dbServiceClient = getServiceClient();
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var loginService = getLoginService();
        var usersWebServer = new ClientWebServerWithBasicSecurity(
                WEB_SERVER_PORT,
                loginService,
                dbServiceClient,
                templateProcessor);
        usersWebServer.start();
        usersWebServer.join();
    }

    private static DbServiceClientImpl getServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Phone.class, Address.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    private static LoginService getLoginService() {
        String hashLoginServiceConfigPath =
                FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        PathResourceFactory pathResourceFactory = new PathResourceFactory();
        Resource configResource = pathResourceFactory.newResource(URI.create(hashLoginServiceConfigPath));
        return new HashLoginService(REALM_NAME, configResource);
    }
}
