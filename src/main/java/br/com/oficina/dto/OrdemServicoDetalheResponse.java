package br.com.oficina.dto;

import br.com.oficina.model.StatusOS;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public record OrdemServicoDetalheResponse(
        Long id,
        @JsonProperty("veiculo_id") Long veiculoId,
        String descricao,
        StatusOS status,
        @JsonProperty("data_abertura") LocalDate dataAbertura,
        List<ItemResponse> servicos) {}
