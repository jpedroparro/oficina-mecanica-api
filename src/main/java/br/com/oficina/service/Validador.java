package br.com.oficina.service;

import br.com.oficina.exception.ApiException;

/** Validações de campos comuns a todos os serviços. */
final class Validador {

    private Validador() {}

    static void corpoObrigatorio(Object corpo) {
        if (corpo == null) {
            throw ApiException.dadosInvalidos("O corpo da requisição é obrigatório.");
        }
    }

    static void obrigatorio(Object valor, String campo) {
        if (valor == null || (valor instanceof String texto && texto.isBlank())) {
            throw ApiException.dadosInvalidos("O campo " + campo + " é obrigatório.");
        }
    }
}
