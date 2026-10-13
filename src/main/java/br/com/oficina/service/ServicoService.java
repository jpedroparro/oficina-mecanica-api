package br.com.oficina.service;

import br.com.oficina.exception.ApiException;
import br.com.oficina.model.Servico;
import br.com.oficina.repository.OrdemServicoRepository;
import br.com.oficina.repository.ServicoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServicoService {

    private final ServicoRepository servicos;
    private final OrdemServicoRepository ordens;

    public ServicoService(ServicoRepository servicos, OrdemServicoRepository ordens) {
        this.servicos = servicos;
        this.ordens = ordens;
    }

    public List<Servico> listar(BigDecimal precoMin, BigDecimal precoMax) {
        return servicos.listar().stream()
                .filter(s -> precoMin == null || s.preco().compareTo(precoMin) >= 0)
                .filter(s -> precoMax == null || s.preco().compareTo(precoMax) <= 0)
                .toList();
    }

    public Servico buscar(long id) {
        return servicos.buscarPorId(id).orElseThrow(() -> ApiException.naoEncontrado("Serviço não encontrado."));
    }

    public Servico criar(Servico dados) {
        validar(dados);
        long id = servicos.proximoId();
        return servicos.salvar(id, new Servico(id, dados.nome(), dados.preco()));
    }

    public Servico atualizar(long id, Servico dados) {
        buscar(id);
        validar(dados);
        return servicos.salvar(id, new Servico(id, dados.nome(), dados.preco()));
    }

    public void excluir(long id) {
        buscar(id);
        if (ordens.servicoEmUso(id)) {
            throw ApiException.emUso("O serviço está em uma ordem de serviço e não pode ser excluído.");
        }
        servicos.excluir(id);
    }

    private void validar(Servico dados) {
        Validador.corpoObrigatorio(dados);
        Validador.obrigatorio(dados.nome(), "nome");
        Validador.obrigatorio(dados.preco(), "preco");
        if (dados.preco().signum() < 0) {
            throw ApiException.dadosInvalidos("O campo preco não pode ser negativo.");
        }
    }
}
