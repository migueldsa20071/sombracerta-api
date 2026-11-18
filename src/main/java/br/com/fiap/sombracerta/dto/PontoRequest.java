package br.com.fiap.sombracerta.dto;

import br.com.fiap.sombracerta.model.TipoPonto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PontoRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
        String endereco,

        @NotBlank(message = "A cidade é obrigatória")
        @Size(max = 80, message = "A cidade deve ter no máximo 80 caracteres")
        String cidade,

        @NotNull(message = "O tipo é obrigatório (PRACA_ARBORIZADA, PONTO_HIDRATACAO, ABRIGO_CLIMATIZADO ou UNIDADE_SAUDE)")
        TipoPonto tipo,

        Boolean ativo
) {
}
