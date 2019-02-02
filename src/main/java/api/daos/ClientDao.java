package api.daos;

import api.entity.Client;

import java.util.List;

public interface ClientDao extends GenericDao<Client, Integer> {

    List<Client> findAll();
}
