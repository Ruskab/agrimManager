package api.stub;

import api.daos.VehicleDao;
import api.entity.Client;
import api.entity.Vehicle;

import java.util.Map;
import java.util.stream.Stream;

class VehicleDaoStub extends GenericDaoStub<Vehicle, Integer> implements VehicleDao {

    VehicleDaoStub(Map<Integer, Vehicle> entities) {
        super(entities);
    }

    @Override
    public Stream<Vehicle> findByClient(Client client) {
        //TODO
        return Stream.empty();
    }

}
