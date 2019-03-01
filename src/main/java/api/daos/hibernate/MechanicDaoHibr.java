package api.daos.hibernate;

import api.daos.MechanicDao;
import api.entity.Mechanic;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.stream.Stream;

class MechanicDaoHibr extends GenericDaoHibr<Mechanic, Integer> implements MechanicDao {

    public MechanicDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

}