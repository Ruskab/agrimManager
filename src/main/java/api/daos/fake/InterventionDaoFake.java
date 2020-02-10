package api.daos.fake;

import api.daos.InterventionDao;
import api.entity.Intervention;

import java.util.Map;

class InterventionDaoFake extends GenericDaoFake<Intervention, Integer> implements InterventionDao {

    InterventionDaoFake(Map<Integer, Intervention> entities) {
        super(entities);
    }

}
