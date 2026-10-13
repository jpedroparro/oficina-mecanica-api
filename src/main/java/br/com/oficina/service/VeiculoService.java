package br.com.oficina.service;

import br.com.oficina.dto.OrdemServicoResumoResponse;
import br.com.oficina.dto.PaginaResponse;
import br.com.oficina.exception.ApiException;
import br.com.oficina.model.Veiculo;
import br.com.oficina.repository.ClienteRepository;
import br.com.oficina.repository.OrdemServicoRepository;
import br.com.oficina.repository.VeiculoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculos;
    private final ClienteRepository clientes;
    private final OrdemServicoRepository ordens;

    public VeiculoService(VeiculoRepository veiculos, ClienteRepository clientes, OrdemServicoRepository ordens) {
        this.veiculos = veiculos;
        this.clientes = clientes;
        this.ordens = ordens;
    }

    public PaginaResponse<Veiculo> listar(String placa, int page, int limit) {
        List<Veiculo> filtrados = veiculos.listar().stream()
                .filter(v -> placa == null || v.placa().equalsIgnoreCase(placa))
                .toList();
        return PaginaResponse.de(filtrados, page, limit);
    }

    public Veiculo buscar(long id) {
        return veiculos.buscarPorId(id).orElseThrow(() -> ApiException.naoEncontrado("Veículo não encontrado."));
    }

    public Veiculo criar(Veiculo dados) {
        validar(dados, null);
        long id = veiculos.proximoId();
        return veiculos.salvar(id, comId(id, dados));
    }

    public Veiculo atualizar(long id, Veiculo dados) {
        buscar(id);
        validar(dados, id);
        return veiculos.salvar(id, comId(id, dados));
    }

    public void excluir(long id) {
        buscar(id);
        if (!ordens.listarPorVeiculo(id).isEmpty()) {
            throw ApiException.emUso("O veículo possui ordens de serviço e não pode ser excluído.");
        }
        veiculos.excluir(id);
    }

    public List<OrdemServicoResumoResponse> listarOrdens(long id) {
        buscar(id);
        return ordens.listarPorVeiculo(id).stream()
                .map(os -> new OrdemServicoResumoResponse(os.id(), os.descricao(), os.status()))
                .toList();
    }

    private Veiculo comId(long id, Veiculo dados) {
        return new Veiculo(id, dados.placa().toUpperCase(), dados.modelo(), dados.ano(), dados.clienteId());
    }

    private void validar(Veiculo dados, Long idAtual) {
        Validador.corpoObrigatorio(dados);
        Validador.obrigatorio(dados.placa(), "placa");
        Validador.obrigatorio(dados.modelo(), "modelo");
        Validador.obrigatorio(dados.ano(), "ano");
        Validador.obrigatorio(dados.clienteId(), "cliente_id");
        if (clientes.buscarPorId(dados.clienteId()).isEmpty()) {
            throw ApiException.naoEncontrado("Cliente não encontrado.");
        }
        if (veiculos.existePlaca(dados.placa(), idAtual)) {
            throw ApiException.conflito("PLACA_DUPLICADA", "Já existe um veículo com esta placa.");
        }
    }
}
