package br.com.fiap.sombracerta.dao;

import br.com.fiap.sombracerta.model.RegistroTemperatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroTemperaturaDAO extends JpaRepository<RegistroTemperatura, Long> {

    List<RegistroTemperatura> findByPontoIdOrderByDataRegistroDesc(Long pontoId);

    boolean existsByPontoId(Long pontoId);
}
