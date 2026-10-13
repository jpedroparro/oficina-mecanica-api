package br.com.oficina.service;

import br.com.oficina.dto.PaginaResponse;
import br.com.oficina.dto.VeiculoResumoResponse;
import br.com.oficina.exception.ApiException;
import br.com.oficina.model.Cliente;
import br.com.oficina.repository.ClienteRepository;
import br.com.oficina.repository.VeiculoRepository;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final ClienteRepository clientes;
    private final VeiculoRepository veiculos;

    public ClienteService(ClienteRepository clientes, VeiculoRepository veiculos) {
        this.clientes = clientes;
        this.veiculos = veiculos;
    }

    public PaginaResponse<Cliente> listar(String nome, int page, int limit) {
        List<Cliente> filtrados = clientes.listar().stream()
                .filter(c -> nome == null || c.nome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
        return PaginaResponse.de(filtrados, page, limit);
    }

    public Cliente buscar(long id) {
        return clientes.buscarPorId(id).orElseThrow(() -> ApiException.naoEncontrado("Cliente não encontrado."));
    }

    public Cliente criar(Cliente dados) {
        validar(dados, null);
        long id = clientes.proximoId();
        return clientes.salvar(id, new Cliente(id, dados.nome(), dados.email(), dados.telefone()));
    }

    public Cliente atualizar(long id, Cliente dados) {
        buscar(id);
        validar(dados, id);
        return clientes.salvar(id, new Cliente(id, dados.nome(), dados.email(), dados.telefone()));
    }

    public void excluir(long id) {
        buscar(id);
        if (!veiculos.listarPorCliente(id).isEmpty()) {
            throw ApiException.emUso("O cliente possui veículos e não pode ser excluído.");
        }
        clientes.excluir(id);
    }

    public List<VeiculoResumoResponse> listarVeiculos(long id) {
        buscar(id);
        return veiculos.listarPorCliente(id).stream()
                .map(v -> new VeiculoResumoResponse(v.id(), v.placa(), v.modelo()))
                .toList();
    }

    private void validar(Cliente dados, Long idAtual) {
        Validador.corpoObrigatorio(dados);
        Validador.obrigatorio(dados.nome(), "nome");
        Validador.obrigatorio(dados.email(), "email");
        Validador.obrigatorio(dados.telefone(), "telefone");
        if (!EMAIL.matcher(dados.email()).matches()) {
            throw ApiException.dadosInvalidos("O campo email é inválido.");
        }
        if (clientes.existeEmail(dados.email(), idAtual)) {
            throw ApiException.conflito("EMAIL_DUPLICADO", "Já existe um cliente com este e-mail.");
        }
    }
}
