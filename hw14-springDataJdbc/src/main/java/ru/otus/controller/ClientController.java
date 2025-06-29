package ru.otus.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.dto.ClientCreateDto;
import ru.otus.dto.ClientViewDto;
import ru.otus.service.ClientsService;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientsService clientsService;

    @GetMapping()
    public String getAllClients(Model model) {
        model.addAttribute("formData", new ClientViewDto());
        getAllClientsToModel(model);
        return "clients";
    }

    @PostMapping("/create")
    public RedirectView createClient(@ModelAttribute("formData") ClientCreateDto formData, Model model) {
        clientsService.createClient(formData);
//        getAllClientsToModel(model);
        return new RedirectView("/clients", true);
    }

    private void getAllClientsToModel(Model model) {
        List<ClientViewDto> clientViewDtos = clientsService.findAllClients().stream()
                .map(clientsService::mapToDto)
                .toList();
        model.addAttribute("clients", clientViewDtos);
    }
}
