package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.repository.CreditCardRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {
    private final ClientService clientService;
    private final CreditCardRepository creditCardRepository;

    public CreditCardService(ClientService clientService, CreditCardRepository creditCardRepository) {
        this.clientService = clientService;
        this.creditCardRepository = creditCardRepository;
    }

    public CreditCardDto[] getAllCreditCardsByClientId(String clientId) {
        this.clientService.findClientById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        return CreditCardDto.fromEntities(
                this.creditCardRepository.findAllById(Integer.parseInt(clientId))
        );
    }

}
