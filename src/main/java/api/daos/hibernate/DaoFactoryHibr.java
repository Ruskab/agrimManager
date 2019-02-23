package api.daos.hibernate;

import api.daos.ClientDao;
import api.daos.DaoFactory;
import api.daos.InterventionDao;
import api.daos.VehicleDao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DaoFactoryHibr extends DaoFactory {

    private EntityManagerFactory entityManagerFactory;

    private ClientDao clientDao;
    private VehicleDao vehicleDao;
    private InterventionDao interventionDao;

    public DaoFactoryHibr() {
        entityManagerFactory = Persistence.createEntityManagerFactory("agrim-pu");
    }

    public ClientDao getClientDao() {
        if (clientDao == null) {
            clientDao = new ClientDaoHibr(entityManagerFactory);
        }
        return clientDao;
    }

    public VehicleDao getVehicleDao() {
        if (vehicleDao == null) {
            vehicleDao = new VehicleDaoHibr(entityManagerFactory);
        }
        return vehicleDao;
    }

    @Override
    public InterventionDao getInterventionDao() {
        if (interventionDao == null) {
            interventionDao = new InterventionDaoHibr(entityManagerFactory);
        }
        return interventionDao;
    }


}
