package api.daos.hibernate;

import api.daos.VehicleDao;
import api.entity.Vehicle;

import javax.persistence.EntityManagerFactory;

class VehicleDaoHibr extends GenericDaoHibr<Vehicle, Integer> implements VehicleDao {

    VehicleDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

}
