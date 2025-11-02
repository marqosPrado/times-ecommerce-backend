package br.com.marcosprado.timesbackend.aggregate.exchange_request;

public enum ExchangeStatus {
    PENDING("Pendente", "Aguardando análise do administrador"),
    APPROVED("Aprovada", "Troca/devolução aprovada pelo administrador"),
    REJECTED("Rejeitada", "Troca/devolução negada pelo administrador"),
    IN_TRANSIT("Em Trânsito", "Produto está sendo enviado de volta"),
    ITEMS_RECEIVED("Itens Recebidos", "Produtos foram recebidos pela empresa"),
    COMPLETED("Concluída", "Troca/devolução finalizada, cupom gerado"),
    CANCELLED("Cancelada", "Solicitação cancelada pelo cliente");

    private final String displayName;
    private final String description;

    ExchangeStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean canBeProcessed() {
        return this == PENDING;
    }

    public boolean canBeSetInTransit() {
        return this == APPROVED;
    }

    public boolean canReceiveItems() {
        return this == IN_TRANSIT;
    }

    public boolean canBeCompleted() {
        return this == ITEMS_RECEIVED;
    }
}
