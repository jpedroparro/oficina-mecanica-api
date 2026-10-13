package br.com.oficina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemRequest(@JsonProperty("servico_id") Long servicoId, Integer quantidade) {}
