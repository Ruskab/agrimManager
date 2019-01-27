package api.daos;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {

    void create(T entity);

    Optional<T> read(ID id);

    void update(T entity);

    void deleteById(ID id);

    List<T> findAll();

}