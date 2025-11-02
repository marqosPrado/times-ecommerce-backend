package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import br.com.marcosprado.timesbackend.aggregate.StateAggregate;
import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.dto.authentication.UserRegisterRequest;
import br.com.marcosprado.timesbackend.dto.client.request.UpdateBasicDataClient;
import br.com.marcosprado.timesbackend.dto.client.response.ClientResponseCompleteDto;
import br.com.marcosprado.timesbackend.repository.AddressRepository;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.repository.StateRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final AddressRepository addressRepository;

    public ClientService(
            ClientRepository clientRepository,
            AddressRepository addressRepository,
            StateRepository stateRepository
            ) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.stateRepository = stateRepository;
    }

    @Transactional
    public ClientAggregate registerClient(UserRegisterRequest dto) {
        if (clientRepository.existsByCpf(dto.cpf()) || clientRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Client already exists");
        }

        ClientAggregate client = new ClientAggregate();
        client.setFullName(dto.fullName());
        client.setBirthDate(dto.birthDate());
        client.setCpf(dto.cpf());
        client.setGender(dto.gender());
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
                        adto.city(),
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
                        cdto.securityCode(),
                        true
                );
                card.setClient(client);
                cards.add(card);
            }
        }
        client.setCreditCards(cards);

        return clientRepository.save(client);
    }

    public ClientResponseCompleteDto getClient(String id) {
        return clientRepository.findById(Integer.parseInt(id))
                .map(ClientResponseCompleteDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }

    public ClientResponseCompleteDto updateBasicData(UpdateBasicDataClient updateBasicDataClient, String id) {
        ClientAggregate clientAggregate = clientRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        clientAggregate.setFullName(updateBasicDataClient.fullName());
        clientAggregate.setPhoneNumber(updateBasicDataClient.phoneNumber());
        clientAggregate.setGender(updateBasicDataClient.gender());

        return ClientResponseCompleteDto.fromEntity(clientRepository.save(clientAggregate));
    }

    @Transactional
    public ClientResponseCompleteDto registerNewAddress(String clientId, AddressDto addressDto) {
        ClientAggregate client = clientRepository.findById(Integer.valueOf(clientId))
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + clientId));

        StateAggregate state = stateRepository.findById(addressDto.stateId())
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado com ID: " + addressDto.stateId()));

        AddressAggregate newAddress = new AddressAggregate(
                addressDto.typeResidence(),
                addressDto.typePlace(),
                addressDto.street(),
                addressDto.number(),
                addressDto.neighborhood(),
                addressDto.cep(),
                addressDto.city(),
                addressDto.country(),
                addressDto.observations(),
                state
        );

        newAddress.setClient(client);

        addressRepository.save(newAddress);

        client.getAddresses().add(newAddress);

        return ClientResponseCompleteDto.fromEntity(client);
    }

    @Transactional
    public ClientResponseCompleteDto registerNewCreditCard(String clientId, CreditCardDto creditCardDto) {
        ClientAggregate client = clientRepository.findById(Integer.valueOf(clientId))
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + clientId));

        if (Boolean.TRUE.equals(creditCardDto.isMain())) {
            client.getCreditCards().forEach(card -> card.setMain(false));
        }

        CreditCardAggregate newCard = new CreditCardAggregate(
                creditCardDto.number(),
                creditCardDto.printedName(),
                creditCardDto.cardFlag(),
                creditCardDto.securityCode(),
                creditCardDto.isMain() != null ? creditCardDto.isMain() : false
        );
        newCard.setClient(client);

        client.getCreditCards().add(newCard);
        clientRepository.save(client);

        return ClientResponseCompleteDto.fromEntity(client);
    }

    @Transactional
    public void removeCreditCard(String clientId, String cardId) {
        ClientAggregate client = clientRepository.findById(Integer.valueOf(clientId))
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + clientId));

        Integer creditCardId = Integer.valueOf(cardId);

        CreditCardAggregate cardToRemove = client.getCreditCards().stream()
                .filter(card -> card.getId().equals(creditCardId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado com ID: " + cardId));

        client.getCreditCards().remove(cardToRemove);

        clientRepository.save(client);
    }

    @Transactional
    public ClientResponseCompleteDto updateAddress(String clientId, String addressId, AddressDto addressDto) {
        ClientAggregate client = clientRepository.findById(Integer.valueOf(clientId))
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + clientId));

        AddressAggregate address = client.getAddresses().stream()
                .filter(a -> a.getId().equals(Integer.valueOf(addressId)))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + addressId));

        address.setTypeResidence(addressDto.typeResidence());
        address.setTypePlace(addressDto.typePlace());
        address.setStreet(addressDto.street());
        address.setNumber(addressDto.number());
        address.setNeighborhood(addressDto.neighborhood());
        address.setCep(addressDto.cep());
        address.setCity(addressDto.city());
        address.setCountry(addressDto.country());
        address.setObservations(addressDto.observations());

        StateAggregate state = stateRepository.findById(addressDto.stateId())
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado com ID: " + addressDto.stateId()));
        address.setState(state);

        clientRepository.save(client);

        return ClientResponseCompleteDto.fromEntity(client);
    }

    public Optional<ClientAggregate> findClientById(Integer clientId) {
        return this.clientRepository.findById(clientId);
    }
}
