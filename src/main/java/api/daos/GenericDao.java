package api.daos;

import java.util.Optional;

public interface GenericDao<T, I> {

    void create(T entity);

    Optional<T> read(I id);

    void update(T entity);

    void deleteById(I id);

}