package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.otus.persistance.crm.model.Address;
import ru.otus.persistance.crm.model.Client;
import ru.otus.persistance.crm.model.Phone;
import ru.otus.persistance.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

@SuppressWarnings({"java:S1989"})
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENT = "clients";

    private final transient DBServiceClient dbServiceClient;
    private final transient TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> all = dbServiceClient.findAll();
        paramsMap.put(TEMPLATE_ATTR_CLIENT, all);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String name = req.getParameter("name");
        String street = req.getParameter("street");
        String phones = req.getParameter("phones");

        List<Phone> phoneList = getPhoneList(phones);
        Client client = dbServiceClient.saveClient(new Client(name, new Address(street), phoneList));
        if (client != null) {
            response.setStatus(200);
            response.sendRedirect("/clients");
        } else {
            response.sendError(500, "Что-то пошло не так...");
        }
    }

    private List<Phone> getPhoneList(String phones) {
        return phones != null ?
                Arrays.stream(phones.split(","))
                        .map(Phone::new)
                        .toList() :
                null;
    }
}
