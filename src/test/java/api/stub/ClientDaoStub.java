package api.stub;

import api.daos.ClientDao;
import api.entity.Client;

import java.util.Map;

class ClientDaoStub extends GenericDaoStub<Client, Integer> implements ClientDao {

    ClientDaoStub(Map<Integer, Client> entities) {
        super(entities);
    }

}
