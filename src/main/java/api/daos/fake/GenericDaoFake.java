package api.daos.fake;

import api.daos.GenericDao;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class GenericDaoFake<T, I> implements GenericDao<T, I> {

    protected Map<Integer, T> entities;

    GenericDaoFake(Map<Integer, T> entities) {
        this.entities = entities;
    }

    @Override
    public void create(T entity) {
        this.entities.put(entities.size() + 1, entity);
    }

    @Override
    public Optional<T> read(I id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public void update(T entity) {
        entities.entrySet().stream()
                .filter(entrySet -> entity.equals(entrySet.getValue()))
                .map(Map.Entry::getKey)
                .forEach(id -> entities.put(id, entity));
    }

    @Override
    public void deleteById(I id) {
        entities.remove(id);
    }

    public Stream<T> findAll() {
        return entities.values().stream();
    }

}
