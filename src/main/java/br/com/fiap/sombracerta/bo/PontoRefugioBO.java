package br.com.fiap.sombracerta.bo;

import br.com.fiap.sombracerta.dao.PontoRefugioDAO;
import br.com.fiap.sombracerta.dao.RegistroTemperaturaDAO;
import br.com.fiap.sombracerta.dto.PontoRequest;
import br.com.fiap.sombracerta.exception.RecursoNaoEncontradoException;
import br.com.fiap.sombracerta.exception.RegraNegocioException;
import br.com.fiap.sombracerta.model.PontoRefugio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PontoRefugioBO {

    private final PontoRefugioDAO pontoDAO;
    private final RegistroTemperaturaDAO registroDAO;

    public PontoRefugioBO(PontoRefugioDAO pontoDAO, RegistroTemperaturaDAO registroDAO) {
        this.pontoDAO = pontoDAO;
        this.registroDAO = registroDAO;
    }

    @Transactional(readOnly = true)
    public List<PontoRefugio> listar(String cidade) {
        if (cidade != null && !cidade.isBlank()) {
            return pontoDAO.findByCidadeIgnoreCase(cidade.trim());
        }
        return pontoDAO.findAll();
    }

    @Transactional(readOnly = true)
    public PontoRefugio buscar(Long id) {
        return pontoDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de refúgio", id));
    }

    @Transactional
    public PontoRefugio criar(PontoRequest req) {
        PontoRefugio p = new PontoRefugio();
        preencher(p, req);
        p.setDataCadastro(LocalDate.now());
        p.setAtivo(req.ativo() == null || req.ativo());
        return pontoDAO.save(p);
    }

    @Transactional
    public PontoRefugio atualizar(Long id, PontoRequest req) {
        PontoRefugio p = buscar(id);
        preencher(p, req);
        if (req.ativo() != null) {
            p.setAtivo(req.ativo());
        }
        return pontoDAO.save(p);
    }

    @Transactional
    public void excluir(Long id) {
        PontoRefugio p = buscar(id);
        if (registroDAO.existsByPontoId(id)) {
            throw new RegraNegocioException(
                    "Não é possível excluir o ponto: existem registros de temperatura associados a ele");
        }
        pontoDAO.delete(p);
    }

    private void preencher(PontoRefugio p, PontoRequest req) {
        p.setNome(req.nome().trim());
        p.setEndereco(req.endereco().trim());
        p.setCidade(req.cidade().trim());
        p.setTipo(req.tipo());
    }
}
