package br.com.fiap.sombracerta.dto;

import br.com.fiap.sombracerta.model.PontoRefugio;
import br.com.fiap.sombracerta.model.TipoPonto;

import java.time.LocalDate;

public record PontoResponse(
        Long id,
        String nome,
        String endereco,
        String cidade,
        TipoPonto tipo,
        LocalDate dataCadastro,
        boolean ativo
) {
    public static PontoResponse de(PontoRefugio p) {
        return new PontoResponse(
                p.getId(), p.getNome(), p.getEndereco(), p.getCidade(),
                p.getTipo(), p.getDataCadastro(), p.isAtivo());
    }
}
