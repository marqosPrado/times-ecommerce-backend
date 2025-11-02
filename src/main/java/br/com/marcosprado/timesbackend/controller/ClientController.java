package br.com.marcosprado.timesbackend.controller;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.dto.authentication.UserRegisterRequest;
import br.com.marcosprado.timesbackend.dto.client.request.UpdateBasicDataClient;
import br.com.marcosprado.timesbackend.dto.client.response.ClientResponseCompleteDto;
import br.com.marcosprado.timesbackend.dto.client.response.UserInfoResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.ClientService;
import br.com.marcosprado.timesbackend.util.SecurityUtils;
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
    public ResponseEntity<ClientAggregate> register(@RequestBody UserRegisterRequest clientRegisterDto) {
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

    @PutMapping("/client/{id}/basic-update")
    public ResponseEntity<ClientResponseCompleteDto> updateBasicData(
            @PathVariable("id") String id,
            @RequestBody UpdateBasicDataClient clientUpdateDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updateBasicData(clientUpdateDto, id));
    }

    @PostMapping("client/{id}/address/new")
    public ResponseEntity<ClientResponseCompleteDto> newAddress(
            @PathVariable("id") String id,
            @RequestBody AddressDto addressDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.registerNewAddress(id,  addressDto));
    }

    @PostMapping("/client/{id}/credit-card/new")
    public ResponseEntity<ClientResponseCompleteDto> newCreditCard(
            @PathVariable("id") String id,
            @RequestBody CreditCardDto creditCardDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.registerNewCreditCard(id, creditCardDto));
    }

    @DeleteMapping("/client/{clientId}/credit-card/{cardId}/remove")
    public ResponseEntity<Void> removeCreditCard(
            @PathVariable("clientId") String clientId,
            @PathVariable("cardId") String cardId

    ) {
        clientService.removeCreditCard(clientId, cardId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/client/{clientId}/address/{addressId}/update")
    public ResponseEntity<ClientResponseCompleteDto> updateAddress(
            @PathVariable("clientId") String clientId,
            @PathVariable("addressId") String addressId,
            @RequestBody AddressDto addressDto
    ) {
        return ResponseEntity.ok(clientService.updateAddress(clientId, addressId, addressDto));
    }

    @GetMapping("/api/client/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser() {
        Integer clientId = SecurityUtils.getCurrentUserId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(clientService.getCurrentUser(clientId)));
    }
}
