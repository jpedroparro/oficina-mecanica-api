package br.com.oficina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record ItemResponse(@JsonProperty("servico_id") Long servicoId, String nome, Integer quantidade, BigDecimal preco) {}
