package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.repository.AddressRepository;
import org.springframework.stereotype.Service;


@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final ClientService clientService;

    public AddressService(AddressRepository addressRepository, ClientService clientService) {
        this.addressRepository = addressRepository;
        this.clientService = clientService;
    }

    public AddressDto[] getAllAddressByClientId(String clientId) {
        ClientAggregate client = this.clientService.findClientById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        return AddressDto.fromEntities(addressRepository.findAll());
    }
}
