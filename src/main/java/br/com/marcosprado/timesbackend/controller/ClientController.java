package br.com.marcosprado.timesbackend.controller;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import br.com.marcosprado.timesbackend.dto.client.ClientDto;
import br.com.marcosprado.timesbackend.dto.client.ClientResponseCompleteDto;
import br.com.marcosprado.timesbackend.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/client/register")
    public ResponseEntity<ClientAggregate> register(@RequestBody ClientDto clientRegisterDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(clientService.registerClient(clientRegisterDto));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar cliente: " + e.getMessage());

        }
    }

    @GetMapping("/client/{id}/all")
    public ResponseEntity<ClientResponseCompleteDto> getClient(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getClient(id));
    }
}
