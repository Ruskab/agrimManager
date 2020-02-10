package api.daos.fake;

import api.daos.ClientDao;
import api.daos.DaoFactory;
import api.daos.InterventionDao;
import api.daos.MechanicDao;
import api.daos.RepairingPackDao;
import api.daos.VehicleDao;

import java.util.HashMap;

public class DaoFactoryFake extends DaoFactory {

    private ClientDao clientDao;
    private VehicleDao vehicleDao;
    private InterventionDao interventionDao;
    private MechanicDao mechanicDao;
    private RepairingPackDao repairingPackDao;

    public DaoFactoryFake() {
    }

    public ClientDao getClientDao() {
        if (clientDao == null) {
            clientDao = new ClientDaoFake(new HashMap<>());
        }
        return clientDao;
    }

    public VehicleDao getVehicleDao() {
        if (vehicleDao == null) {
            vehicleDao = new VehicleDaoFake(new HashMap<>());
        }
        return vehicleDao;
    }

    @Override
    public InterventionDao getInterventionDao() {
        if (interventionDao == null) {
            interventionDao = new InterventionDaoFake(new HashMap<>());
        }
        return interventionDao;
    }

    @Override
    public MechanicDao getMechanicDao() {
        if (mechanicDao == null) {
            mechanicDao = new MechanicDaoFake(new HashMap<>());
        }
        return mechanicDao;
    }

    @Override
    public RepairingPackDao getRepairingPackDao() {
        if (repairingPackDao == null) {
            repairingPackDao = new RepairingPackDaoFake(new HashMap<>());
        }
        return repairingPackDao;
    }


}
