package api.stub;

import api.daos.InterventionDao;
import api.entity.Intervention;

import java.util.Map;

class InterventionDaoStub extends GenericDaoStub<Intervention, Integer> implements InterventionDao {

    InterventionDaoStub(Map<Integer, Intervention> entities) {
        super(entities);
    }

}
