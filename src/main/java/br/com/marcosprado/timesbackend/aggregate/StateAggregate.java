package br.com.marcosprado.timesbackend.aggregate;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "STATE")
public class StateAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "sta_id")
    private Integer id;

    @Column(name = "sta_state", length = 20, nullable = false)
    private String state;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressAggregate> addresses = new ArrayList<>();

    public StateAggregate() {}

    public StateAggregate(String state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<AddressAggregate> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressAggregate> addresses) {
        this.addresses = addresses;
    }
}
