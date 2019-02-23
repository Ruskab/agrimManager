package api.daos;

import api.entity.Client;

import java.util.stream.Stream;

public interface ClientDao extends GenericDao<Client, Integer> {

    Stream<Client> findAll();
}
