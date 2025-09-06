package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import br.com.marcosprado.timesbackend.dto.admin.ClientFilterDto;
import br.com.marcosprado.timesbackend.dto.client.ClientResponseDto;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.specification.ClientSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminService {
    private final ClientRepository clientRepository;

    public AdminService(
            ClientRepository clientRepository
    ) {
        this.clientRepository = clientRepository;
    }

    public List<ClientResponseDto> search(ClientFilterDto filter) {
        List<ClientAggregate> clientAggregates = clientRepository.findAll(
                Specification.allOf(
                        ClientSpecification.hasName(filter.name()),
                        ClientSpecification.hasCpf(filter.cpf()),
                        ClientSpecification.hasGender(filter.gender()),
                        ClientSpecification.hasEmail(filter.email()),
                        ClientSpecification.hasPhone(filter.phoneNumber()),
                        ClientSpecification.hasActive(filter.active())
                )
        );

        if (clientAggregates.isEmpty()) {
            throw new RuntimeException("Nenhum cliente encontrado");
        }

        return clientAggregates.stream()
                .map(ClientResponseDto::from)
                .toList();
    }
}
