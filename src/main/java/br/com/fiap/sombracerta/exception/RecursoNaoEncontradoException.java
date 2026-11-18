package br.com.fiap.sombracerta.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " não encontrada(o) com id " + id);
    }
}
