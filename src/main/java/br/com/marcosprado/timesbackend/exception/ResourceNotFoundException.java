package br.com.marcosprado.timesbackend.exception;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }

    public static ResourceNotFoundException voucherNotFound(String identifier) {
        return new ResourceNotFoundException(
                String.format("Cupom com identificador '%s' não foi encontrado.", identifier)
        );
    }

    public static ResourceNotFoundException voucherNotFoundById(Long id) {
        return new ResourceNotFoundException(
                String.format("Cupom com o id '%d' não foi encontrado.", id)
        );
    }

    public static ResourceNotFoundException clientNotFound(Integer clientId) {
        return new ResourceNotFoundException(
                String.format("Cliente com o id '%d' não foi encontrado.", clientId)
        );
    }

    public static ResourceNotFoundException productNotFound(Long productId) {
        return new ResourceNotFoundException(
                String.format("Produto com o id '%d' não foi encontrado", productId)
        );
    }

    public static ResourceNotFoundException addressNotFoud(Integer addressId) {
        return new ResourceNotFoundException(
                String.format("Endereço com o id '%d' não foi encontrado", addressId)
        );
    }

    public static ResourceNotFoundException creditCardNotFound(Integer creditCardId) {
        return new ResourceNotFoundException(
                String.format("Cartão de crédito com o id '%d' não foi encontrado", creditCardId)
        );
    }

    public static ResourceNotFoundException userNotFound(String email) {
        return new ResourceNotFoundException(
                String.format("Usuário não encontrado com o email '%s'.", email)
        );
    }

    public static ResourceNotFoundException userNotFound() {
        return new ResourceNotFoundException(
                "Usuário não encontrado."
        );
    }

    public static ResourceNotFoundException purchaseOrderNotFound() {
        return new ResourceNotFoundException(
                "Order de compra não encontrada"
        );
    }

    public static ResourceNotFoundException orderItemNotFound(Long itemId) {
        return new ResourceNotFoundException(
                String.format("Item de pedido com o id %d não foi encontrado", itemId)
        );
    }

    public static ResourceNotFoundException exchangeRequestNotFound() {
        return new ResourceNotFoundException(
                "Pedido de troca não encontrado"
        );
    }

    public static ResourceNotFoundException exchangeRequestVoucherNotFound(Long id) {
        return new ResourceNotFoundException(
                String.format("Cupom de troca com o id: %d não foi encontrado", id)
        );
    }
}
