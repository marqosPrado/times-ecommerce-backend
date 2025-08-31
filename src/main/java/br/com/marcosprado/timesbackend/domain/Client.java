package br.com.marcosprado.timesbackend.domain;

import br.com.marcosprado.timesbackend.enums.TypePhoneNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Client {
    private String id;
    private String fullName;
    private LocalDate birthDate;
    private String cpf;
    private String email;
    private String password;
    private TypePhoneNumber typePhoneNumber;
    private String phoneNumber;
    private boolean active;
    private BigDecimal credit;
    private List<Address> addresses;
    private List<CreditCard>  creditCards;

    private Client(
            String fullName,
            LocalDate birthDate,
            String cpf,
            String email,
            String password,
            TypePhoneNumber typePhoneNumber,
            String phoneNumber,
            boolean active,
            BigDecimal credit,
            List<Address> addresses,
            List<CreditCard> creditCards
    ) {
        this.fullName = fullName.trim();
        this.birthDate = birthDate;
        this.cpf = cpf.trim();
        this.email = email.trim();
        this.password = password.trim();
        this.typePhoneNumber = typePhoneNumber;
        this.phoneNumber = phoneNumber.trim();
        this.active = active;
        this.credit = credit;
        this.addresses = addresses;
        this.creditCards = creditCards;
    }

    public static Client create(
            String fullName,
            LocalDate birthDate,
            String cpf,
            String email,
            String password,
            TypePhoneNumber typePhoneNumber,
            String phoneNumber,
            boolean active,
            BigDecimal credit,
            List<Address> addresses,
            List<CreditCard> creditCards
    ) {
        return new Client(
                fullName,
                birthDate,
                cpf,
                email,
                password,
                typePhoneNumber,
                phoneNumber,
                active,
                credit,
                addresses,
                creditCards
        );
    }

    public String getId() {
        return id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }
}
