package br.com.oficina.service;

import br.com.oficina.dto.ItemRequest;
import br.com.oficina.dto.ItemResponse;
import br.com.oficina.dto.OrdemServicoDetalheResponse;
import br.com.oficina.dto.OrdemServicoRequest;
import br.com.oficina.dto.PaginaResponse;
import br.com.oficina.exception.ApiException;
import br.com.oficina.model.ItemOrdemServico;
import br.com.oficina.model.OrdemServico;
import br.com.oficina.model.Servico;
import br.com.oficina.model.StatusOS;
import br.com.oficina.repository.OrdemServicoRepository;
import br.com.oficina.repository.ServicoRepository;
import br.com.oficina.repository.VeiculoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository ordens;
    private final VeiculoRepository veiculos;
    private final ServicoRepository servicos;

    public OrdemServicoService(
            OrdemServicoRepository ordens, VeiculoRepository veiculos, ServicoRepository servicos) {
        this.ordens = ordens;
        this.veiculos = veiculos;
        this.servicos = servicos;
    }

    public PaginaResponse<OrdemServico> listar(String status, int page, int limit) {
        StatusOS filtro = status == null ? null : converterStatus(status);
        List<OrdemServico> filtradas = ordens.listar().stream()
                .filter(os -> filtro == null || os.status() == filtro)
                .toList();
        return PaginaResponse.de(filtradas, page, limit);
    }

    public OrdemServico buscar(long id) {
        return ordens.buscarPorId(id)
                .orElseThrow(() -> ApiException.naoEncontrado("Ordem de serviço não encontrada."));
    }

    public OrdemServicoDetalheResponse buscarComServicos(long id) {
        OrdemServico os = buscar(id);
        return new OrdemServicoDetalheResponse(
                os.id(), os.veiculoId(), os.descricao(), os.status(), os.dataAbertura(), listarServicos(id));
    }

    public OrdemServico abrir(OrdemServicoRequest dados) {
        Validador.corpoObrigatorio(dados);
        Validador.obrigatorio(dados.veiculoId(), "veiculo_id");
        Validador.obrigatorio(dados.descricao(), "descricao");
        if (veiculos.buscarPorId(dados.veiculoId()).isEmpty()) {
            throw ApiException.naoEncontrado("Veículo não encontrado.");
        }
        long id = ordens.proximoId();
        return ordens.salvar(
                id, new OrdemServico(id, dados.veiculoId(), dados.descricao(), StatusOS.ABERTA, LocalDate.now()));
    }

    public OrdemServico atualizar(long id, OrdemServicoRequest dados) {
        OrdemServico atual = buscar(id);
        Validador.corpoObrigatorio(dados);
        Validador.obrigatorio(dados.descricao(), "descricao");
        Validador.obrigatorio(dados.status(), "status");
        StatusOS status = converterStatus(dados.status());
        return ordens.salvar(
                id, new OrdemServico(id, atual.veiculoId(), dados.descricao(), status, atual.dataAbertura()));
    }

    public void excluir(long id) {
        if (buscar(id).status() != StatusOS.ABERTA) {
            throw ApiException.emUso("Só é possível excluir OS com status ABERTA.");
        }
        ordens.excluir(id);
    }

    public List<ItemResponse> listarServicos(long ordemId) {
        buscar(ordemId);
        return ordens.listarItens(ordemId).stream().map(this::paraResposta).toList();
    }

    public ItemResponse adicionarServico(long ordemId, ItemRequest dados) {
        buscar(ordemId);
        Validador.corpoObrigatorio(dados);
        Validador.obrigatorio(dados.servicoId(), "servico_id");
        Validador.obrigatorio(dados.quantidade(), "quantidade");
        if (dados.quantidade() < 1) {
            throw ApiException.dadosInvalidos("O campo quantidade deve ser maior ou igual a 1.");
        }
        if (servicos.buscarPorId(dados.servicoId()).isEmpty()) {
            throw ApiException.naoEncontrado("Serviço não encontrado.");
        }
        boolean jaAdicionado =
                ordens.listarItens(ordemId).stream().anyMatch(item -> item.servicoId().equals(dados.servicoId()));
        if (jaAdicionado) {
            throw ApiException.conflito("SERVICO_DUPLICADO", "O serviço já está nesta ordem de serviço.");
        }
        ItemOrdemServico item = new ItemOrdemServico(dados.servicoId(), dados.quantidade());
        ordens.adicionarItem(ordemId, item);
        return paraResposta(item);
    }

    public void removerServico(long ordemId, long servicoId) {
        buscar(ordemId);
        if (!ordens.removerItem(ordemId, servicoId)) {
            throw ApiException.naoEncontrado("Serviço não encontrado nesta ordem de serviço.");
        }
    }

    private ItemResponse paraResposta(ItemOrdemServico item) {
        Servico servico = servicos.buscarPorId(item.servicoId()).orElseThrow();
        return new ItemResponse(servico.id(), servico.nome(), item.quantidade(), servico.preco());
    }

    private StatusOS converterStatus(String status) {
        try {
            return StatusOS.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw ApiException.dadosInvalidos("Status inválido. Use ABERTA, EM_ANDAMENTO ou CONCLUIDA.");
        }
    }
}
