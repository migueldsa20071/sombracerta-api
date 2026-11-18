package br.com.fiap.sombracerta.controller;

import br.com.fiap.sombracerta.bo.RegistroTemperaturaBO;
import br.com.fiap.sombracerta.dto.RegistroRequest;
import br.com.fiap.sombracerta.dto.RegistroResponse;
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
@RequestMapping("/registros")
public class RegistroTemperaturaController {

    private final RegistroTemperaturaBO bo;

    public RegistroTemperaturaController(RegistroTemperaturaBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<RegistroResponse> listar(@RequestParam(required = false) Long pontoId) {
        return bo.listar(pontoId).stream().map(RegistroResponse::de).toList();
    }

    @GetMapping("/{id}")
    public RegistroResponse buscar(@PathVariable Long id) {
        return RegistroResponse.de(bo.buscar(id));
    }

    @PostMapping
    public ResponseEntity<RegistroResponse> criar(@RequestBody @Valid RegistroRequest req) {
        RegistroResponse criado = RegistroResponse.de(bo.criar(req));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public RegistroResponse atualizar(@PathVariable Long id, @RequestBody @Valid RegistroRequest req) {
        return RegistroResponse.de(bo.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
