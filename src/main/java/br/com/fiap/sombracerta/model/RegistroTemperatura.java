package br.com.fiap.sombracerta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TB_REGISTRO_TEMPERATURA")
public class RegistroTemperatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REGISTRO")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_PONTO", nullable = false)
    private PontoRefugio ponto;

    @Column(name = "DT_REGISTRO", nullable = false)
    private LocalDate dataRegistro;

    @Column(name = "NR_TEMPERATURA_C", nullable = false, precision = 5, scale = 2)
    private BigDecimal temperaturaC;

    @Column(name = "NR_UMIDADE_PCT", nullable = false, precision = 5, scale = 2)
    private BigDecimal umidadePct;

    public RegistroTemperatura() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PontoRefugio getPonto() {
        return ponto;
    }

    public void setPonto(PontoRefugio ponto) {
        this.ponto = ponto;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public BigDecimal getTemperaturaC() {
        return temperaturaC;
    }

    public void setTemperaturaC(BigDecimal temperaturaC) {
        this.temperaturaC = temperaturaC;
    }

    public BigDecimal getUmidadePct() {
        return umidadePct;
    }

    public void setUmidadePct(BigDecimal umidadePct) {
        this.umidadePct = umidadePct;
    }
}
