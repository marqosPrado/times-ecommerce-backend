package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.*;
import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.dto.ClientDto;
import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.repository.StateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final StateRepository stateRepository;

    public ClientService(ClientRepository clientRepository, StateRepository stateRepository) {
        this.clientRepository = clientRepository;
        this.stateRepository = stateRepository;
    }

    @Transactional
    public ClientAggregate registerClient(ClientDto dto) {
        if (clientRepository.existsByCpf(dto.cpf()) || clientRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Client already exists");
        }

        ClientAggregate client = new ClientAggregate();
        client.setFullName(dto.fullName());
        client.setBirthDate(dto.birthDate());
        client.setCpf(dto.cpf());
        client.setEmail(dto.email());
        client.setPassword(dto.password());
        client.setTypePhoneNumber(dto.typePhoneNumber());
        client.setPhoneNumber(dto.phoneNumber());
        client.setActive(true);
        client.setCredit(BigDecimal.ZERO);

        List<AddressAggregate> addresses = new ArrayList<>();
        if (dto.addresses() != null) {
            for (AddressDto adto : dto.addresses()) {
                StateAggregate state = stateRepository.findById(adto.stateId())
                        .orElseThrow(() -> new RuntimeException("State not found with id: " + adto.stateId()));
                AddressAggregate address = new AddressAggregate(
                        adto.typeResidence(),
                        adto.typePlace(),
                        adto.street(),
                        adto.number(),
                        adto.neighborhood(),
                        adto.cep(),
                        adto.country(),
                        adto.observations(),
                        state
                );
                address.setClient(client);
                addresses.add(address);
            }
        }
        client.setAddresses(addresses);

        List<CreditCardAggregate> cards = new ArrayList<>();
        if (dto.creditCards() != null) {
            for (CreditCardDto cdto : dto.creditCards()) {
                CreditCardAggregate card = new CreditCardAggregate(
                        cdto.number(),
                        cdto.printedName(),
                        cdto.cardFlag(),
                        cdto.securityCode()
                );
                card.setClient(client);
                cards.add(card);
            }
        }
        client.setCreditCards(cards);

        return clientRepository.save(client);
    }
}
