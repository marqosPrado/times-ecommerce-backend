package br.com.marcosprado.timesbackend.aggregate;

import br.com.marcosprado.timesbackend.enums.CardFlag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity(name = "CREDIT_CARDS")
public class CreditCardAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crd_id")
    private Integer id;

    @Column(name = "crd_number", nullable = false, length = 16)
    private String number;

    @Column(name = "crd_printed_name", nullable = false, length = 50)
    private String printedName;

    @Column(name = "crd_card_flag", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardFlag cardFlag;

    @Column(name = "crd_security_code", nullable = false, length = 4)
    private String securityCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "crd_cli_id")
    @JsonIgnore
    private ClientAggregate client;

    public CreditCardAggregate() {}

    public CreditCardAggregate(
            String number,
            String printedName,
            CardFlag cardFlag,
            String securityCode
    ) {
        this.number = number;
        this.printedName = printedName;
        this.cardFlag = cardFlag;
        this.securityCode = securityCode;
    }

    public Integer getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrintedName() {
        return printedName;
    }

    public void setPrintedName(String printedName) {
        this.printedName = printedName;
    }

    public CardFlag getCardFlag() {
        return cardFlag;
    }

    public void setCardFlag(CardFlag cardFlag) {
        this.cardFlag = cardFlag;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public ClientAggregate getClient() {
        return client;
    }

    public void setClient(ClientAggregate client) {
        this.client = client;
    }
}
