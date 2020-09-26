package api.daos.hibernate;

import api.daos.InterventionDao;
import api.entity.Intervention;

import javax.persistence.EntityManagerFactory;

class InterventionDaoHibr extends GenericDaoHibr<Intervention, Integer> implements InterventionDao {

    InterventionDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}
