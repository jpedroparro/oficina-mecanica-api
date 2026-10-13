package br.com.oficina.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Veiculo(Long id, String placa, String modelo, Integer ano, @JsonProperty("cliente_id") Long clienteId) {}
