package api.daos.hibernate;

import api.daos.InterventionDao;
import api.entity.Intervention;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.stream.Stream;

class InterventionDaoHibr extends GenericDaoHibr<Intervention, Integer> implements InterventionDao {

    InterventionDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    //todo moverlo a GenericDaoHibr y que sirva para todas las entidades
    //todo hacer que Query sea mediante Criteria
    public Stream<Intervention> findAll() {
        Query query = entityManager.createQuery("select i from Intervention i");
        return query.getResultStream();
    }

}
