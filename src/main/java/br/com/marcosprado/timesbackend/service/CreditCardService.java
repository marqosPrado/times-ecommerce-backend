package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.repository.CreditCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService {
    private final ClientService clientService;
    private final CreditCardRepository creditCardRepository;

    public CreditCardService(ClientService clientService, CreditCardRepository creditCardRepository) {
        this.clientService = clientService;
        this.creditCardRepository = creditCardRepository;
    }

    public CreditCardDto[] getAllCreditCardsByClientId(Integer clientId) {
        var client = this.clientService.findClientById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        return CreditCardDto.fromEntities(
                this.creditCardRepository.findAllByClient(client)
        );
    }

    public CreditCardDto registerCreditCard(CreditCardDto creditCardDto, Integer clientId) {
        ClientAggregate clientAggregate = this.clientService.findClientById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        CreditCardAggregate newCreditCard = creditCardDto.toEntity();
        newCreditCard.setClient(clientAggregate);

        if (newCreditCard.getMain()) {
            List<CreditCardAggregate> creditCards = this.creditCardRepository.findAllById(clientId);
            creditCards.forEach(creditCard -> {
                creditCard.setMain(false);
                creditCardRepository.save(creditCard);
            });
        }

        return CreditCardDto.fromEntity(this.creditCardRepository.save(newCreditCard));
    }

    public Optional<CreditCardAggregate> findCreditCardById(Integer creditCardId) {
        return creditCardRepository.findById(creditCardId);
    }
}
