package api.daos.hibernate;

import api.daos.InterventionDao;
import api.daos.VehicleDao;
import api.entity.Client;
import api.entity.Intervention;
import api.entity.Vehicle;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

class InterventionDaoHibr extends GenericDaoHibr<Intervention, Integer> implements InterventionDao {

    InterventionDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    //todo moverlo a GenericDaoHibr y que sirva para todas las entidades
    //todo hacer que Query sea mediante Criteria
    //todo usar getResultStream y JPA 2.2
    public List<Intervention> findAll() {
        Query query = entityManager.createQuery("select i from Intervention i");
        return query.getResultList();
    }

}
