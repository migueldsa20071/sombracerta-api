package br.com.fiap.sombracerta.dto;

import br.com.fiap.sombracerta.model.RegistroTemperatura;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistroResponse(
        Long id,
        Long pontoId,
        String pontoNome,
        LocalDate dataRegistro,
        BigDecimal temperaturaC,
        BigDecimal umidadePct,
        String nivelRisco
) {
    public static RegistroResponse de(RegistroTemperatura r) {
        return new RegistroResponse(
                r.getId(),
                r.getPonto().getId(),
                r.getPonto().getNome(),
                r.getDataRegistro(),
                r.getTemperaturaC(),
                r.getUmidadePct(),
                classificarRisco(r.getTemperaturaC()));
    }

    // Faixas de risco de calor: < 30 BAIXO | 30-34,99 MODERADO | 35-39,99 ALTO | >= 40 EXTREMO
    private static String classificarRisco(BigDecimal temperaturaC) {
        if (temperaturaC.compareTo(BigDecimal.valueOf(40)) >= 0) {
            return "EXTREMO";
        }
        if (temperaturaC.compareTo(BigDecimal.valueOf(35)) >= 0) {
            return "ALTO";
        }
        if (temperaturaC.compareTo(BigDecimal.valueOf(30)) >= 0) {
            return "MODERADO";
        }
        return "BAIXO";
    }
}
