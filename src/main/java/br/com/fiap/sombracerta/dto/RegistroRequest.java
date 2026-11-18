package br.com.fiap.sombracerta.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistroRequest(
        @NotNull(message = "O id do ponto de refúgio é obrigatório")
        Long pontoId,

        @NotNull(message = "A data do registro é obrigatória")
        @PastOrPresent(message = "A data do registro não pode ser futura")
        LocalDate dataRegistro,

        @NotNull(message = "A temperatura em °C é obrigatória")
        @DecimalMin(value = "-10.00", message = "A temperatura deve ser maior ou igual a -10 °C")
        @DecimalMax(value = "60.00", message = "A temperatura deve ser menor ou igual a 60 °C")
        BigDecimal temperaturaC,

        @NotNull(message = "A umidade relativa (%) é obrigatória")
        @DecimalMin(value = "0.00", message = "A umidade deve ser maior ou igual a 0%")
        @DecimalMax(value = "100.00", message = "A umidade deve ser menor ou igual a 100%")
        BigDecimal umidadePct
) {
}
