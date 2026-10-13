package br.com.oficina.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record OrdemServico(
        Long id,
        @JsonProperty("veiculo_id") Long veiculoId,
        String descricao,
        StatusOS status,
        @JsonProperty("data_abertura") LocalDate dataAbertura) {}
