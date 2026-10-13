package br.com.oficina.exception;

import org.springframework.http.HttpStatus;

/** Erro de negócio com o código HTTP e o código de erro que a API devolve. */
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String erro;

    public ApiException(HttpStatus status, String erro, String mensagem) {
        super(mensagem);
        this.status = status;
        this.erro = erro;
    }

    public static ApiException naoEncontrado(String mensagem) {
        return new ApiException(HttpStatus.NOT_FOUND, "RECURSO_NAO_ENCONTRADO", mensagem);
    }

    public static ApiException dadosInvalidos(String mensagem) {
        return new ApiException(HttpStatus.BAD_REQUEST, "DADOS_INVALIDOS", mensagem);
    }

    public static ApiException conflito(String erro, String mensagem) {
        return new ApiException(HttpStatus.CONFLICT, erro, mensagem);
    }

    public static ApiException emUso(String mensagem) {
        return conflito("RECURSO_EM_USO", mensagem);
    }

    public static ApiException naoAutenticado() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "NAO_AUTENTICADO", "Token ausente ou inválido.");
    }

    public static ApiException credenciaisInvalidas() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "CREDENCIAIS_INVALIDAS", "E-mail ou senha incorretos.");
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }
}
