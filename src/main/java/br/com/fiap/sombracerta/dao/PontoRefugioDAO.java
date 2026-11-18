package br.com.fiap.sombracerta.dao;

import br.com.fiap.sombracerta.model.PontoRefugio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoRefugioDAO extends JpaRepository<PontoRefugio, Long> {

    List<PontoRefugio> findByCidadeIgnoreCase(String cidade);
}
