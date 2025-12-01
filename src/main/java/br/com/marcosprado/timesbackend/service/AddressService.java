package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.StateAggregate;
import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.repository.AddressRepository;
import br.com.marcosprado.timesbackend.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final StateRepository stateRepository;
    private final ClientService clientService;

    public AddressService(
            AddressRepository addressRepository,
            StateRepository stateRepository,
            ClientService clientService
    ) {
        this.addressRepository = addressRepository;
        this.stateRepository = stateRepository;
        this.clientService = clientService;
    }

    public AddressDto[] getAllAddressByClientId(Integer clientId) {
        var client = this.clientService.findClientById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        return AddressDto.fromEntities(client.getAddresses());
    }

    public AddressDto registerAddress(AddressDto addressDto, Integer clientId) {
        ClientAggregate client = this.clientService.findClientById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        StateAggregate state = this.stateRepository.findById(addressDto.stateId())
                .orElseThrow(() -> new RuntimeException("State not found"));

        AddressAggregate newAddress = addressDto.toEntity(state);
        newAddress.setClient(client);

        return AddressDto.fromEntity(this.addressRepository.save(newAddress));
    }

    public Optional<AddressAggregate> findAddressById(Integer addressId) {
        return addressRepository.findById(addressId);
    }
}
