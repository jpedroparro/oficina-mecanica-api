package br.com.oficina.dto;

import br.com.oficina.model.StatusOS;

public record OrdemServicoResumoResponse(Long id, String descricao, StatusOS status) {}
