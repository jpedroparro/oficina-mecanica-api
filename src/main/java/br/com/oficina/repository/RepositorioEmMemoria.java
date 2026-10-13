package br.com.oficina.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/** Armazenamento em memória, ordenado por id. Os dados se perdem ao reiniciar a aplicação. */
public abstract class RepositorioEmMemoria<T> {

    private final Map<Long, T> dados = new ConcurrentSkipListMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    public long proximoId() {
        return sequencia.incrementAndGet();
    }

    public T salvar(long id, T entidade) {
        dados.put(id, entidade);
        return entidade;
    }

    public Optional<T> buscarPorId(long id) {
        return Optional.ofNullable(dados.get(id));
    }

    public List<T> listar() {
        return new ArrayList<>(dados.values());
    }

    public void excluir(long id) {
        dados.remove(id);
    }
}
