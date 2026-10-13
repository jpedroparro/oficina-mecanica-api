package br.com.oficina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** POST usa veiculo_id e descricao; PUT usa descricao e status. */
public record OrdemServicoRequest(@JsonProperty("veiculo_id") Long veiculoId, String descricao, String status) {}
