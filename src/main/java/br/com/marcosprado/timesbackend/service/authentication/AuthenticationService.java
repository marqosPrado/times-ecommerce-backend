package br.com.marcosprado.timesbackend.service.authentication;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import br.com.marcosprado.timesbackend.aggregate.StateAggregate;
import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.dto.authentication.AuthenticationResponse;
import br.com.marcosprado.timesbackend.dto.authentication.LoginRequest;
import br.com.marcosprado.timesbackend.dto.authentication.UserRegisterRequest;
import br.com.marcosprado.timesbackend.dto.client.response.UserInfoResponse;
import br.com.marcosprado.timesbackend.exception.OperationNotAllowedException;
import br.com.marcosprado.timesbackend.exception.ResourceAlreadyExistsException;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.repository.StateRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StateRepository stateRepository;

    public AuthenticationService(
            ClientRepository clientRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            StateRepository stateRepository
    ) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.stateRepository = stateRepository;
    }

    @Transactional
    public AuthenticationResponse register(UserRegisterRequest dto) {
        if (clientRepository.existsByCpf(dto.cpf()) || clientRepository.existsByEmail(dto.email())) {
            throw ResourceAlreadyExistsException.userAlreadyExists();
        }

        ClientAggregate client = new ClientAggregate();
        client.setFullName(dto.fullName());
        client.setBirthDate(dto.birthDate());
        client.setCpf(dto.cpf());
        client.setGender(dto.gender());
        client.setEmail(dto.email());
        client.setPassword(passwordEncoder.encode(dto.password()));
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

        clientRepository.save(client);

        var jwtToken = jwtService.generateToken(client);
        var refreshToken = jwtService.generateRefreshToken(client);

        return new AuthenticationResponse(
                jwtToken,
                refreshToken,
                UserInfoResponse.from(client)
        );
    }

    public AuthenticationResponse login(LoginRequest request) {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException exception) {
            throw OperationNotAllowedException.invalidCredentials();
        }

        var client = clientRepository.findByEmail(request.email())
                .orElseThrow(() -> ResourceNotFoundException.userNotFound(request.email()));

        var jwtToken = jwtService.generateToken(client);
        var refreshToken = jwtService.generateRefreshToken(client);

        return new AuthenticationResponse(
                jwtToken,
                refreshToken,
                UserInfoResponse.from((ClientAggregate) client)
        );
    }
}
