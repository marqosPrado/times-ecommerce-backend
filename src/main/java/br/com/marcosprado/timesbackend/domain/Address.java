package br.com.marcosprado.timesbackend.domain;

import br.com.marcosprado.timesbackend.enums.TypePlace;
import br.com.marcosprado.timesbackend.enums.TypeResidence;

public class Address {
    private int id;
    private TypeResidence typeResidence;
    private TypePlace typePlace;
    private String street;
    private int number;
    private String neighborhood;
    private String cep;
    private String country;
    private String observation;
    private State state;

    private Address(
       TypeResidence typeResidence,
       TypePlace typePlace,
       String street,
       int number,
       String neighborhood,
       String cep,
       String country,
       String observation
    ) {
        this.typeResidence = typeResidence;
        this.typePlace = typePlace;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.cep = cep;
        this.country = country;
        this.observation = observation;
    }

    public int getId() {
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
