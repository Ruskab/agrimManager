package api.daos.hibernate;

import api.daos.VehicleDao;
import api.entity.Client;
import api.entity.Vehicle;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

class VehicleDaoHibr extends GenericDaoHibr<Vehicle, Integer> implements VehicleDao {

    VehicleDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    //todo moverlo a GenericDaoHibr y que sirva para todas las entidades
    //todo hacer que Query sea mediante Criteria
    public List<Vehicle> findAll() {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Query query = entityManager.createQuery("select v from Vehicle v");
        entityTransaction.commit();
        return query.getResultList();
    }

    @Override
    //todo revisar el commit si tiene que ir despues de gerResultList
    public List<Vehicle> findByClient(Client client) {
        Query query = entityManager.createQuery("select v from Vehicle v where v.client = :client");
        query.setParameter("client", client);
        return query.getResultList();
    }

}
