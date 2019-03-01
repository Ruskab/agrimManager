package api.daos.hibernate;

import api.daos.VehicleDao;
import api.entity.Client;
import api.entity.Vehicle;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.stream.Stream;

class VehicleDaoHibr extends GenericDaoHibr<Vehicle, Integer> implements VehicleDao {

    VehicleDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    public Stream<Vehicle> findByClient(Client client) {
        Query query = entityManager.createQuery("select v from Vehicle v where v.client = :client");
        query.setParameter("client", client);
        return query.getResultStream();
    }

}
