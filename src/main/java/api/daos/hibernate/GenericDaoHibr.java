package api.daos.hibernate;

import api.daos.GenericDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public class GenericDaoHibr<T,ID> implements GenericDao<T,ID> {

    protected Class<T> entityClass;

    private final static Logger LOGGER = LogManager.getLogger(GenericDaoHibr.class);

    protected EntityManager entityManager;

    public GenericDaoHibr(EntityManagerFactory entityManagerFactory) {
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
    public Optional<T> read(ID id) {
        return Optional.ofNullable(entityManager.find(entityClass,id)) ;
    }

    @Override
    public void update(T entity) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.merge(entity);
        entityTransaction.commit();
    }

    @Override
    public void deleteById(ID id) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        T entity = entityManager.find(entityClass,id);
        if (entity == null){
            LOGGER.warn("not found");
        }
        entityManager.remove(entity);
        entityTransaction.commit();
    }

    @Override
    public List<T> findAll() {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Query query = entityManager.createQuery("select c from Client c");
        entityTransaction.commit();
        return query.getResultList();
    }
}
