package api.daos;

import api.entity.Client;
import api.entity.Vehicle;

import java.util.stream.Stream;

public interface VehicleDao extends GenericDao<Vehicle, Integer> {

    Stream<Vehicle> findAll();

    Stream<Vehicle> findByClient(Client client);

}
