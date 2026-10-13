package br.com.oficina.controller;

import br.com.oficina.model.Servico;
import br.com.oficina.service.ServicoService;
import java.math.BigDecimal;
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
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Servico> listar(
            @RequestParam(name = "preco_min", required = false) BigDecimal precoMin,
            @RequestParam(name = "preco_max", required = false) BigDecimal precoMax) {
        return service.listar(precoMin, precoMax);
    }

    @GetMapping("/{id}")
    public Servico buscar(@PathVariable long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servico criar(@RequestBody Servico dados) {
        return service.criar(dados);
    }

    @PutMapping("/{id}")
    public Servico atualizar(@PathVariable long id, @RequestBody Servico dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id) {
        service.excluir(id);
    }
}
