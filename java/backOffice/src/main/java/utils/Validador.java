package utils;

public final class Validador {

    private Validador() {}

    public static void exigirNaoVazio(String valor, String campo) {
        if (valor == null || valor.isBlank())
            throw new IllegalArgumentException(campo + " é obrigatório.");
    }

    public static void exigirEmail(String email) {
        if (email != null && !email.isBlank() && !email.contains("@"))
            throw new IllegalArgumentException("E-mail inválido: " + email);
    }

    public static void exigirIntervalo(int valor, int min, int max, String campo) {
        if (valor < min || valor > max)
            throw new IllegalArgumentException(
                    campo + " deve estar entre " + min + " e " + max + ".");
    }
}