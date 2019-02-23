package api.daos;

import api.entity.Intervention;

import java.util.stream.Stream;

public interface InterventionDao extends GenericDao<Intervention, Integer> {

    Stream<Intervention> findAll();

}
