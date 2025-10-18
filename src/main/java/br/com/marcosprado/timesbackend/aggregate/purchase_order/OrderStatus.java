package br.com.marcosprado.timesbackend.aggregate.purchase_order;

public enum OrderStatus {
    PROCESSING("Em Processamento", "Aguardando validação do pagamento"),
    APPROVED("Aprovada", "Pagamento aprovado, aguardando despacho"),
    REJECTED("Reprovada", "Pagamento não aprovado"),
    IN_TRANSIT("Em Trânsito", "Produtos em rota de entrega"),
    DELIVERED("Entregue", "Produtos entregues ao cliente"),
    EXCHANGE_REQUESTED("Em Troca", "Cliente solicitou troca"),
    EXCHANGE_AUTHORIZED("Troca Autorizada", "Troca autorizada pelo administrador"),
    EXCHANGED("Trocado", "Itens de troca recebidos e processados");

    private final String displayName;
    private final String description;

    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEditable() {
        return this == PROCESSING;
    }

    public boolean canBeCancelled() {
        return this == PROCESSING || this == APPROVED;
    }

    public boolean canRequestExchange() {
        return this == DELIVERED;
    }
}