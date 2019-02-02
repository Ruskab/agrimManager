package api.daos.hibernate;

import api.daos.VehicleDao;
import api.entity.Vehicle;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

class VehicleDaoHibr extends GenericDaoHibr<Vehicle, Integer> implements VehicleDao {

    VehicleDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public List<Vehicle> findAll() {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Query query = entityManager.createQuery("select c from Vehicle c");
        entityTransaction.commit();
        return query.getResultList();
    }

}
