package br.com.oficina.dto;

import br.com.oficina.exception.ApiException;
import java.util.List;

public record PaginaResponse<T>(int page, int limit, int total, List<T> data) {

    public static final int LIMITE_MAXIMO = 100;

    /** Recorta a lista completa na página pedida. {@code page} começa em 1. */
    public static <T> PaginaResponse<T> de(List<T> todos, int page, int limit) {
        if (page < 1) {
            throw ApiException.dadosInvalidos("O parâmetro page deve ser maior ou igual a 1.");
        }
        if (limit < 1 || limit > LIMITE_MAXIMO) {
            throw ApiException.dadosInvalidos("O parâmetro limit deve estar entre 1 e " + LIMITE_MAXIMO + ".");
        }
        long inicio = Math.min((long) (page - 1) * limit, todos.size());
        long fim = Math.min(inicio + limit, todos.size());
        return new PaginaResponse<>(page, limit, todos.size(), todos.subList((int) inicio, (int) fim));
    }
}
