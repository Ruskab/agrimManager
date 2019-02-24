package api.daos;

import api.entity.Intervention;
import api.entity.Mechanic;

import java.util.stream.Stream;

public interface MechanicDao extends GenericDao<Mechanic, Integer> {

    Stream<Mechanic> findAll();

}
