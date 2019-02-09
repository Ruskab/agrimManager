package api.daos;

import api.entity.Vehicle;

import java.util.List;

public interface VehicleDao extends GenericDao<Vehicle, Integer> {

    List<Vehicle> findAll();

    List<Vehicle> findByClientId(int id);

}
