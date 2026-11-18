package br.com.fiap.sombracerta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "TB_PONTO_REFUGIO")
public class PontoRefugio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PONTO")
    private Long id;

    @Column(name = "NM_PONTO", nullable = false, length = 120)
    private String nome;

    @Column(name = "DS_ENDERECO", nullable = false, length = 200)
    private String endereco;

    @Column(name = "NM_CIDADE", nullable = false, length = 80)
    private String cidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_PONTO", nullable = false, length = 20)
    private TipoPonto tipo;

    @Column(name = "DT_CADASTRO", nullable = false)
    private LocalDate dataCadastro;

    @Column(name = "ST_ATIVO", nullable = false)
    private boolean ativo;

    public PontoRefugio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public TipoPonto getTipo() {
        return tipo;
    }

    public void setTipo(TipoPonto tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
