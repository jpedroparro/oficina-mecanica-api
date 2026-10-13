package br.com.oficina.repository;

import br.com.oficina.model.Cliente;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepository extends RepositorioEmMemoria<Cliente> {

    public boolean existeEmail(String email, Long idIgnorado) {
        return listar().stream().anyMatch(c -> c.email().equalsIgnoreCase(email) && !c.id().equals(idIgnorado));
    }
}
