package br.com.marcosprado.timesbackend.aggregate;

import br.com.marcosprado.timesbackend.enums.TypePhoneNumber;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CLIENTS")
public class ClientAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cli_full_name", length = 50, nullable = false)
    private String fullName;

    @Column(name = "cli_date_birth", nullable = false)
    private LocalDate birthDate;

    @Column(name = "cli_cpf", length = 11, nullable = false)
    private String cpf;

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

    @Column(name = "cli_credit", precision = 15, scale = 2, nullable = false)
    private BigDecimal credit;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressAggregate>  addresses = new ArrayList<>();

    public ClientAggregate() {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
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

    public Boolean getActive() {
        return active;
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
}
