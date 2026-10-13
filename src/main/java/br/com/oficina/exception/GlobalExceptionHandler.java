package br.com.oficina.exception;

import br.com.oficina.dto.ErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Converte qualquer exceção no formato único de erro: status, erro e mensagem. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErroResponse> tratarErroDeNegocio(ApiException e) {
        return resposta(e.getStatus(), e.getErro(), e.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroResponse> tratarRequisicaoMalFormada(Exception e) {
        return resposta(HttpStatus.BAD_REQUEST, "DADOS_INVALIDOS", "JSON ou parâmetro com formato inválido.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroInesperado(Exception e) {
        // Erros do próprio Spring (rota inexistente, método não permitido) mantêm o código HTTP deles
        if (e instanceof ErrorResponse erroSpring) {
            HttpStatus status = HttpStatus.valueOf(erroSpring.getStatusCode().value());
            if (status == HttpStatus.NOT_FOUND) {
                return resposta(status, "RECURSO_NAO_ENCONTRADO", "Rota não encontrada.");
            }
            if (status.is4xxClientError()) {
                return resposta(status, "DADOS_INVALIDOS", "Requisição inválida.");
            }
        }
        log.error("Falha inesperada", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "ERRO_INTERNO", "Erro interno. Tente novamente mais tarde.");
    }

    private ResponseEntity<ErroResponse> resposta(HttpStatus status, String erro, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), erro, mensagem));
    }
}
