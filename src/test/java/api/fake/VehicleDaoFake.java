package api.fake;

import api.daos.VehicleDao;
import api.entity.Client;
import api.entity.Vehicle;

import java.util.Map;
import java.util.stream.Stream;

class VehicleDaoFake extends GenericDaoFake<Vehicle, Integer> implements VehicleDao {

    VehicleDaoFake(Map<Integer, Vehicle> entities) {
        super(entities);
    }

    @Override
    public Stream<Vehicle> findByClient(Client client) {
        //TODO
        return Stream.empty();
    }

}
