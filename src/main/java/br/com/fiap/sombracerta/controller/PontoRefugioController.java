package br.com.fiap.sombracerta.controller;

import br.com.fiap.sombracerta.bo.PontoRefugioBO;
import br.com.fiap.sombracerta.dto.PontoRequest;
import br.com.fiap.sombracerta.dto.PontoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pontos")
public class PontoRefugioController {

    private final PontoRefugioBO bo;

    public PontoRefugioController(PontoRefugioBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<PontoResponse> listar(@RequestParam(required = false) String cidade) {
        return bo.listar(cidade).stream().map(PontoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public PontoResponse buscar(@PathVariable Long id) {
        return PontoResponse.de(bo.buscar(id));
    }

    @PostMapping
    public ResponseEntity<PontoResponse> criar(@RequestBody @Valid PontoRequest req) {
        PontoResponse criado = PontoResponse.de(bo.criar(req));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public PontoResponse atualizar(@PathVariable Long id, @RequestBody @Valid PontoRequest req) {
        return PontoResponse.de(bo.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
