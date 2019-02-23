package api.daos.hibernate;

import api.daos.ClientDao;
import api.entity.Client;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.stream.Stream;

class ClientDaoHibr extends GenericDaoHibr<Client, Integer> implements ClientDao {

    ClientDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public Stream<Client> findAll() {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Query query = entityManager.createQuery("select c from Client c");
        entityTransaction.commit();
        return query.getResultStream();
    }

}
