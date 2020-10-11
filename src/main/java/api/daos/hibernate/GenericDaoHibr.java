package api.daos.hibernate;

import api.daos.GenericDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.stream.Stream;

public class GenericDaoHibr<T, I> implements GenericDao<T, I> {

    private static final Logger LOGGER = LogManager.getLogger(GenericDaoHibr.class);
    Class<T> entityClass;
    EntityManager entityManager;

    GenericDaoHibr(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
        ParameterizedType genericSupperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSupperclass.getActualTypeArguments()[0];
    }

    @Override
    public void create(T entity) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(entity);
        entityTransaction.commit();
    }

    @Override
    public Optional<T> read(I id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    public void update(T entity) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.merge(entity);
        entityTransaction.commit();
    }

    @Override
    public void deleteById(I id) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        T entity = entityManager.find(entityClass, id);
        if (entity == null) {
            LOGGER.warn("not found");
        }
        entityManager.remove(entity);
        entityTransaction.commit();
    }

    public Stream<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        return allQuery.getResultStream();
    }

}
