package api.daos;

import api.entity.Client;
import api.entity.Intervention;
import api.entity.Vehicle;

import java.util.List;

public interface InterventionDao extends GenericDao<Intervention, Integer> {

    List<Intervention> findAll();

}
