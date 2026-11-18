package br.com.fiap.sombracerta.bo;

import br.com.fiap.sombracerta.dao.RegistroTemperaturaDAO;
import br.com.fiap.sombracerta.dto.RegistroRequest;
import br.com.fiap.sombracerta.exception.RecursoNaoEncontradoException;
import br.com.fiap.sombracerta.exception.RegraNegocioException;
import br.com.fiap.sombracerta.model.PontoRefugio;
import br.com.fiap.sombracerta.model.RegistroTemperatura;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistroTemperaturaBO {

    private final RegistroTemperaturaDAO registroDAO;
    private final PontoRefugioBO pontoBO;

    public RegistroTemperaturaBO(RegistroTemperaturaDAO registroDAO, PontoRefugioBO pontoBO) {
        this.registroDAO = registroDAO;
        this.pontoBO = pontoBO;
    }

    @Transactional(readOnly = true)
    public List<RegistroTemperatura> listar(Long pontoId) {
        if (pontoId != null) {
            pontoBO.buscar(pontoId); // garante 404 se o ponto não existir
            return registroDAO.findByPontoIdOrderByDataRegistroDesc(pontoId);
        }
        return registroDAO.findAll();
    }

    @Transactional(readOnly = true)
    public RegistroTemperatura buscar(Long id) {
        return registroDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Registro de temperatura", id));
    }

    @Transactional
    public RegistroTemperatura criar(RegistroRequest req) {
        PontoRefugio ponto = pontoBO.buscar(req.pontoId());
        validarPontoAtivo(ponto);
        RegistroTemperatura r = new RegistroTemperatura();
        r.setPonto(ponto);
        preencher(r, req);
        return registroDAO.save(r);
    }

    @Transactional
    public RegistroTemperatura atualizar(Long id, RegistroRequest req) {
        RegistroTemperatura r = buscar(id);
        PontoRefugio ponto = pontoBO.buscar(req.pontoId());
        validarPontoAtivo(ponto);
        r.setPonto(ponto);
        preencher(r, req);
        return registroDAO.save(r);
    }

    @Transactional
    public void excluir(Long id) {
        registroDAO.delete(buscar(id));
    }

    private void validarPontoAtivo(PontoRefugio ponto) {
        if (!ponto.isAtivo()) {
            throw new RegraNegocioException(
                    "O ponto de refúgio " + ponto.getId() + " está inativo e não aceita novos registros");
        }
    }

    private void preencher(RegistroTemperatura r, RegistroRequest req) {
        r.setDataRegistro(req.dataRegistro());
        r.setTemperaturaC(req.temperaturaC());
        r.setUmidadePct(req.umidadePct());
    }
}
