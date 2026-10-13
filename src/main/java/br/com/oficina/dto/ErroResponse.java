package br.com.oficina.dto;

/** Formato único de erro da API. */
public record ErroResponse(int status, String erro, String mensagem) {}
