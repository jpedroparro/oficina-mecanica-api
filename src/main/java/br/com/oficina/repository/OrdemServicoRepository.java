package br.com.oficina.repository;

import br.com.oficina.model.ItemOrdemServico;
import br.com.oficina.model.OrdemServico;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;

@Repository
public class OrdemServicoRepository extends RepositorioEmMemoria<OrdemServico> {

    // Itens (serviços) de cada OS, indexados pelo id da OS
    private final Map<Long, List<ItemOrdemServico>> itensPorOrdem = new ConcurrentHashMap<>();

    public List<OrdemServico> listarPorVeiculo(long veiculoId) {
        return listar().stream().filter(os -> os.veiculoId() == veiculoId).toList();
    }

    public List<ItemOrdemServico> listarItens(long ordemId) {
        return itensPorOrdem.computeIfAbsent(ordemId, id -> new CopyOnWriteArrayList<>());
    }

    public void adicionarItem(long ordemId, ItemOrdemServico item) {
        listarItens(ordemId).add(item);
    }

    public boolean removerItem(long ordemId, long servicoId) {
        return listarItens(ordemId).removeIf(item -> item.servicoId() == servicoId);
    }

    public boolean servicoEmUso(long servicoId) {
        return itensPorOrdem.values().stream()
                .flatMap(List::stream)
                .anyMatch(item -> item.servicoId() == servicoId);
    }

    @Override
    public void excluir(long id) {
        super.excluir(id);
        itensPorOrdem.remove(id);
    }
}
