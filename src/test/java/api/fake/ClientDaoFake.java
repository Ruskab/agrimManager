package api.fake;

import api.daos.ClientDao;
import api.entity.Client;

import java.util.Map;

class ClientDaoFake extends GenericDaoFake<Client, Integer> implements ClientDao {

    ClientDaoFake(Map<Integer, Client> entities) {
        super(entities);
    }

}
