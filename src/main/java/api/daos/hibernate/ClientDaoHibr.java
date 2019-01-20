package api.daos.hibernate;

import api.daos.ClientDao;
import api.entity.Client;

import javax.persistence.EntityManagerFactory;

public class ClientDaoHibr extends GenericDaoHibr<Client,Integer> implements ClientDao {

    public ClientDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}
