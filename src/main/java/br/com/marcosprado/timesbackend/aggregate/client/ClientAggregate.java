package br.com.marcosprado.timesbackend.aggregate.client;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import br.com.marcosprado.timesbackend.enums.Gender;
import br.com.marcosprado.timesbackend.enums.TypePhoneNumber;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CLIENTS")
public class ClientAggregate implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cli_id")
    private Integer id;

    @Column(name = "cli_full_name", length = 50, nullable = false)
    private String fullName;

    @Column(name = "cli_date_birth", nullable = false)
    private LocalDate birthDate;

    @Column(name = "cli_cpf", length = 11, nullable = false)
    private String cpf;

    @Column(name = "cli_gender", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "cli_email", length = 50, nullable = false)
    private String email;

    @Column(name = "cli_password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "cli_type_phone_number", nullable = false)
    private TypePhoneNumber typePhoneNumber;

    @Column(name = "cli_phone_number", length = 14, nullable = false)
    private String phoneNumber;

    @Column(name = "cli_is_active", nullable = false)
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.CLIENT;

    @Column(name = "cli_credit", precision = 15, scale = 2, nullable = false)
    private BigDecimal credit;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressAggregate>  addresses;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCardAggregate>  creditCards;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<ExchangeRequest> exchangeRequests = new HashSet<>();

    public ClientAggregate() {}

    public ClientAggregate(
            String fullName,
            LocalDate birthDate,
            String cpf,
            Gender gender,
            String email,
            String password,
            TypePhoneNumber typePhoneNumber,
            String phoneNumber,
            Boolean active,
            BigDecimal credit,
            List<AddressAggregate> addresses,
            List<CreditCardAggregate> creditCards
    ) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.cpf = cpf;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.typePhoneNumber = typePhoneNumber;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.credit = credit;
        this.addresses = addresses;
        this.creditCards = creditCards;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Boolean getActive() {
        return active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TypePhoneNumber getTypePhoneNumber() {
        return typePhoneNumber;
    }

    public void setTypePhoneNumber(TypePhoneNumber typePhoneNumber) {
        this.typePhoneNumber = typePhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public List<AddressAggregate> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressAggregate> addresses) {
        this.addresses = addresses;
    }

    public List<CreditCardAggregate> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardAggregate> creditCards) {
        this.creditCards = creditCards;
    }
}
