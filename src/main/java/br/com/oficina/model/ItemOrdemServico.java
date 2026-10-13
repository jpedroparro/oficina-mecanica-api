package br.com.oficina.model;

/** Ligação N:N entre ordem de serviço e serviço. */
public record ItemOrdemServico(Long servicoId, Integer quantidade) {}
