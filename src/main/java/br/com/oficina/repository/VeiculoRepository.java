package br.com.oficina.repository;

import br.com.oficina.model.Veiculo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class VeiculoRepository extends RepositorioEmMemoria<Veiculo> {

    /** Indica se outro veículo (diferente de {@code idIgnorado}) já usa a placa. */
    public boolean existePlaca(String placa, Long idIgnorado) {
        return listar().stream().anyMatch(v -> v.placa().equalsIgnoreCase(placa) && !v.id().equals(idIgnorado));
    }

    public List<Veiculo> listarPorCliente(long clienteId) {
        return listar().stream().filter(v -> v.clienteId() == clienteId).toList();
    }
}
