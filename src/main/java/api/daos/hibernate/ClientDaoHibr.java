package api.daos.hibernate;

import api.daos.ClientDao;
import api.entity.Client;

import javax.persistence.EntityManagerFactory;

class ClientDaoHibr extends GenericDaoHibr<Client, Integer> implements ClientDao {

    ClientDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }



}
