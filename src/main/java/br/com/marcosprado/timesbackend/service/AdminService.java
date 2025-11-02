package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.dto.admin.ClientFilterDto;
import br.com.marcosprado.timesbackend.dto.admin.UpdateClientStatusDto;
import br.com.marcosprado.timesbackend.dto.client.response.UserInfoResponse;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.specification.ClientSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final ClientRepository clientRepository;

    public AdminService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<UserInfoResponse> search(ClientFilterDto filter, Pageable pageable) {

        Specification<ClientAggregate> spec = Specification
                .where(ClientSpecification.hasName(filter.name()))
                .and(ClientSpecification.hasCpf(filter.cpf()))
                .and(ClientSpecification.hasGender(filter.gender()))
                .and(ClientSpecification.hasEmail(filter.email()))
                .and(ClientSpecification.hasPhone(filter.phoneNumber()))
                .and(ClientSpecification.hasActive(filter.active()));

        Page<ClientAggregate> clientPage = clientRepository.findAll(spec, pageable);

        return clientPage.map(UserInfoResponse::from);
    }

    @Transactional
    public UserInfoResponse updateStatus(Integer id, UpdateClientStatusDto dto) {
        ClientAggregate client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));

        client.setActive(dto.active());

        ClientAggregate updatedClient = clientRepository.save(client);

        return UserInfoResponse.from(updatedClient);
    }
}
