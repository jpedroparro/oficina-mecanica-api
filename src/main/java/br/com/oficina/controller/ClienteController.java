package br.com.oficina.controller;

import br.com.oficina.dto.PaginaResponse;
import br.com.oficina.dto.VeiculoResumoResponse;
import br.com.oficina.model.Cliente;
import br.com.oficina.service.ClienteService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public PaginaResponse<Cliente> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return service.listar(nome, page, limit);
    }

    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente criar(@RequestBody Cliente dados) {
        return service.criar(dados);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable long id, @RequestBody Cliente dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id) {
        service.excluir(id);
    }

    @GetMapping("/{id}/veiculos")
    public List<VeiculoResumoResponse> listarVeiculos(@PathVariable long id) {
        return service.listarVeiculos(id);
    }
}
