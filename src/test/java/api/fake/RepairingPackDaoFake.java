package api.fake;

import api.daos.RepairingPackDao;
import api.entity.RepairingPack;

import java.util.Map;

class RepairingPackDaoFake extends GenericDaoFake<RepairingPack, Integer> implements RepairingPackDao {
    public RepairingPackDaoFake(Map<Integer, RepairingPack> entities) {
        super(entities);
    }
}
