package br.com.marcosprado.timesbackend.domain;

import br.com.marcosprado.timesbackend.enums.CardFlag;

public class CreditCard {
    private int id;
    private String number;
    private String printedName;
    private CardFlag flag;
    private String securityCode;

    private CreditCard(
            String number,
            String printedName,
            CardFlag flag,
            String securityCode
    ) {
        this.number = number;
        this.printedName = printedName;
        this.flag = flag;
        this.securityCode = securityCode;
    }

    public static CreditCard create(
            String number,
            String printedName,
            CardFlag flag,
            String securityCode
    ) {
        return new CreditCard(number, printedName, flag, securityCode);
    }

    public int getId() {
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

    public CardFlag getFlag() {
        return flag;
    }

    public void setFlag(CardFlag flag) {
        this.flag = flag;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
