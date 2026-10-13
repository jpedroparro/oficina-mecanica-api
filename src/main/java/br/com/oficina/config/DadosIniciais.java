package br.com.oficina.config;

import br.com.oficina.model.Servico;
import br.com.oficina.repository.ServicoRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DadosIniciais implements CommandLineRunner {

    private final ServicoRepository servicos;

    public DadosIniciais(ServicoRepository servicos) {
        this.servicos = servicos;
    }

    @Override
    public void run(String... args) {
        adicionar("Troca de óleo", "189.90");
        adicionar("Alinhamento", "140.00");
    }

    private void adicionar(String nome, String preco) {
        long id = servicos.proximoId();
        servicos.salvar(id, new Servico(id, nome, new BigDecimal(preco)));
    }
}
