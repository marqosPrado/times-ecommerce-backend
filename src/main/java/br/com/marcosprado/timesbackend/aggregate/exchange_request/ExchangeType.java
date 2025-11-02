package br.com.marcosprado.timesbackend.aggregate.exchange_request;

public enum ExchangeType {
    EXCHANGE("Troca", "Cliente deseja trocar produto"),
    RETURN("Devolução", "Cliente deseja devolver produto");

    private final String displayName;
    private final String description;

    ExchangeType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
