package api.daos.hibernate;

import api.daos.ClientDao;
import api.daos.DaoFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DaoFactoryHibr extends DaoFactory {

    private EntityManagerFactory entityManagerFactory;

    private ClientDao clientDao;

    public DaoFactoryHibr(){
        entityManagerFactory = Persistence.createEntityManagerFactory("agrim-pu");
    }

    public ClientDao getClientDao(){
        if(clientDao == null){
            clientDao = new ClientDaoHibr(entityManagerFactory);
        }
        return clientDao;
    }

}
