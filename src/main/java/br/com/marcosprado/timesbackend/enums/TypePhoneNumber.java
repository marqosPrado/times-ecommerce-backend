package br.com.marcosprado.timesbackend.enums;

public enum TypePhoneNumber {
    CELULAR,
    FIXO;

    public static TypePhoneNumber fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Tipo do telefone não pode ser nulo.");
        }

        return switch (value.toUpperCase()) {
            case "CELULAR" -> CELULAR;
            case "FIXO" -> FIXO;
            default -> throw new IllegalArgumentException("Tipo de telefone inválido: " + value);
        };
    }
}
