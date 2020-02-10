package api.stub;

import api.ClientMother;
import api.InterventionMother;
import api.MechanicMother;
import api.RepairingPackMother;
import api.daos.ClientDao;
import api.daos.DaoFactory;
import api.daos.InterventionDao;
import api.daos.MechanicDao;
import api.daos.RepairingPackDao;
import api.daos.VehicleDao;
import api.entity.Client;
import api.entity.Intervention;
import api.entity.Mechanic;
import api.entity.RepairingPack;
import api.entity.Role;
import api.entity.Vehicle;
import api.entity.builder.VehicleBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static api.entity.InterventionType.CAFFE;
import static api.entity.builder.VehicleBuilder.REGISTRATION_PLATE;

public class DaoFactoryStub extends DaoFactory {

    private ClientDao clientDao;
    private VehicleDao vehicleDao;
    private InterventionDao interventionDao;
    private MechanicDao mechanicDao;
    private RepairingPackDao repairingPackDao;

    private Map<Integer, Mechanic> mechanics = Collections.unmodifiableMap(new HashMap<Integer, Mechanic>() {
        {
            put(1, MechanicMother.withInterventions(List.of(
                    InterventionMother.withVehicle(new VehicleBuilder(REGISTRATION_PLATE).byDefault().createVehicle()),
                    InterventionMother.withInterventionType(CAFFE),
                    InterventionMother.notFinished()
            )));
            put(2, MechanicMother.withRoles(List.of(Role.ADMIN)));
        }
    });

    private Map<Integer, Vehicle> vehicles = Collections.unmodifiableMap(new HashMap<Integer, Vehicle>() {
        {
            put(1, new VehicleBuilder(REGISTRATION_PLATE).setClient(ClientMother.client()).createVehicle());
            put(2, new VehicleBuilder(REGISTRATION_PLATE).setClient(ClientMother.withFullName("otherClient")).createVehicle());
        }
    });

    private Map<Integer, Client> clients = Collections.unmodifiableMap(new HashMap<Integer, Client>() {
        {
            put(1, ClientMother.client());
            put(2, ClientMother.withFullName("otherClient"));
        }
    });

    private Map<Integer, Intervention> interventions = Collections.unmodifiableMap(new HashMap<Integer, Intervention>() {
        {
            put(1, InterventionMother.intervention());
            put(2, InterventionMother.notFinished());
        }
    });

    private Map<Integer, RepairingPack> repairingPacks = Collections.unmodifiableMap(new HashMap<Integer, RepairingPack>() {
        {
            put(1, RepairingPackMother.repairingPack());
            put(2, RepairingPackMother.withInvoicedHours(20));
        }
    });


    public DaoFactoryStub() {
    }

    public ClientDao getClientDao() {
        if (clientDao == null) {
            clientDao = new ClientDaoStub(clients);
        }
        return clientDao;
    }

    public VehicleDao getVehicleDao() {
        if (vehicleDao == null) {
            vehicleDao = new VehicleDaoStub(vehicles);
        }
        return vehicleDao;
    }

    @Override
    public InterventionDao getInterventionDao() {
        if (interventionDao == null) {
            interventionDao = new InterventionDaoStub(interventions);
        }
        return interventionDao;
    }

    @Override
    public MechanicDao getMechanicDao() {
        if (mechanicDao == null) {
            mechanicDao = new MechanicDaoStub(mechanics);
        }
        return mechanicDao;
    }

    @Override
    public RepairingPackDao getRepairingPackDao() {
        if (repairingPackDao == null) {
            repairingPackDao = new RepairingPackDaoStub(repairingPacks);
        }
        return repairingPackDao;
    }


}
