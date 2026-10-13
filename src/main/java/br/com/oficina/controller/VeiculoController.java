package br.com.oficina.controller;

import br.com.oficina.dto.OrdemServicoResumoResponse;
import br.com.oficina.dto.PaginaResponse;
import br.com.oficina.model.Veiculo;
import br.com.oficina.service.VeiculoService;
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
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @GetMapping
    public PaginaResponse<Veiculo> listar(
            @RequestParam(required = false) String placa,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return service.listar(placa, page, limit);
    }

    @GetMapping("/{id}")
    public Veiculo buscar(@PathVariable long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Veiculo criar(@RequestBody Veiculo dados) {
        return service.criar(dados);
    }

    @PutMapping("/{id}")
    public Veiculo atualizar(@PathVariable long id, @RequestBody Veiculo dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id) {
        service.excluir(id);
    }

    @GetMapping("/{id}/ordens-servico")
    public List<OrdemServicoResumoResponse> listarOrdens(@PathVariable long id) {
        return service.listarOrdens(id);
    }
}
