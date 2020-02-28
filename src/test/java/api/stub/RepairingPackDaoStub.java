package api.stub;

import api.daos.RepairingPackDao;
import api.entity.RepairingPack;

import java.util.Map;

class RepairingPackDaoStub extends GenericDaoStub<RepairingPack, Integer> implements RepairingPackDao {
    public RepairingPackDaoStub(Map<Integer, RepairingPack> entities) {
        super(entities);
    }
}
