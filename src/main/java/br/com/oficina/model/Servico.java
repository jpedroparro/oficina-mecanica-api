package br.com.oficina.model;

import java.math.BigDecimal;

public record Servico(Long id, String nome, BigDecimal preco) {}
