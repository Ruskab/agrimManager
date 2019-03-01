package api.daos.hibernate;

import api.daos.MechanicDao;
import api.entity.Mechanic;

import javax.persistence.EntityManagerFactory;

class MechanicDaoHibr extends GenericDaoHibr<Mechanic, Integer> implements MechanicDao {

    public MechanicDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

}
