package br.com.oficina.controller;

import br.com.oficina.dto.ItemRequest;
import br.com.oficina.dto.ItemResponse;
import br.com.oficina.dto.OrdemServicoDetalheResponse;
import br.com.oficina.dto.OrdemServicoRequest;
import br.com.oficina.dto.PaginaResponse;
import br.com.oficina.model.OrdemServico;
import br.com.oficina.service.OrdemServicoService;
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
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoService service;

    public OrdemServicoController(OrdemServicoService service) {
        this.service = service;
    }

    @GetMapping
    public PaginaResponse<OrdemServico> listar(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return service.listar(status, page, limit);
    }

    @GetMapping("/{id}")
    public OrdemServicoDetalheResponse buscar(@PathVariable long id) {
        return service.buscarComServicos(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServico abrir(@RequestBody OrdemServicoRequest dados) {
        return service.abrir(dados);
    }

    @PutMapping("/{id}")
    public OrdemServico atualizar(@PathVariable long id, @RequestBody OrdemServicoRequest dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id) {
        service.excluir(id);
    }

    @GetMapping("/{id}/servicos")
    public List<ItemResponse> listarServicos(@PathVariable long id) {
        return service.listarServicos(id);
    }

    @PostMapping("/{id}/servicos")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse adicionarServico(@PathVariable long id, @RequestBody ItemRequest dados) {
        return service.adicionarServico(id, dados);
    }

    @DeleteMapping("/{id}/servicos/{servicoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerServico(@PathVariable long id, @PathVariable long servicoId) {
        service.removerServico(id, servicoId);
    }
}
