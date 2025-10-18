package br.com.marcosprado.timesbackend.aggregate;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.enums.TypePlace;
import br.com.marcosprado.timesbackend.enums.TypeResidence;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "ADDRESS")
public class AddressAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "add_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "add_type_residence", length = 20, nullable = false)
    private TypeResidence typeResidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "add_type_place", length = 7, nullable = false)
    private TypePlace  typePlace;

    @Column(name = "add_street", length = 50, nullable = false)
    private String street;

    @Column(name = "add_number", nullable = false)
    private Integer number;

    @Column(name = "add_neighborhood", length = 20, nullable = false)
    private String neighborhood;

    @Column(name = "add_cep", length = 10, nullable = false)
    private String cep;

    @Column(name = "add_city", length = 50, nullable = false)
    private String city;

    @Column(name = "add_country", length = 10, nullable = false)
    private String country;

    @Column(name = "add_observations")
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "add_sta_id", nullable = false)
    private StateAggregate state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "add_cli_id", nullable = false)
    @JsonIgnore
    private ClientAggregate client;

    @OneToMany(mappedBy = "address")
    private Set<PurchaseOrder> purchaseOrders;

    public AddressAggregate() {}

    public AddressAggregate(
            TypeResidence typeResidence,
            TypePlace typePlace,
            String street,
            Integer number,
            String neighborhood,
            String cep,
            String city,
            String country,
            String observations,
            StateAggregate state
    ) {
        this.typeResidence = typeResidence;
        this.typePlace = typePlace;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.cep = cep;
        this.city = city;
        this.country = country;
        this.observations = observations;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public TypeResidence getTypeResidence() {
        return typeResidence;
    }

    public void setTypeResidence(TypeResidence typeResidence) {
        this.typeResidence = typeResidence;
    }

    public TypePlace getTypePlace() {
        return typePlace;
    }

    public void setTypePlace(TypePlace typePlace) {
        this.typePlace = typePlace;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public StateAggregate getState() {
        return state;
    }

    public void setState(StateAggregate state) {
        this.state = state;
    }

    public ClientAggregate getClient() {
        return client;
    }

    public void setClient(ClientAggregate client) {
        this.client = client;
    }
}