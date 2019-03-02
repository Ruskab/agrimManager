package api.daos.hibernate;

import api.daos.RepairingPackDao;
import api.entity.RepairingPack;

import javax.persistence.EntityManagerFactory;

class RepairingPackDaoHibr extends GenericDaoHibr<RepairingPack, Integer> implements RepairingPackDao {
    public RepairingPackDaoHibr(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}
