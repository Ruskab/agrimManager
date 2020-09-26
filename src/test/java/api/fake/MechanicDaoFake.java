package api.fake;

import api.daos.MechanicDao;
import api.entity.Mechanic;

import java.util.Map;
import java.util.stream.Stream;

class MechanicDaoFake extends GenericDaoFake<Mechanic, Integer> implements MechanicDao {

    public MechanicDaoFake(Map<Integer, Mechanic> entities) {
        super(entities);
    }

    @Override
    public Stream<Mechanic> findBy(String name) {
        //TODO
        return Stream.empty();
    }

    @Override
    public Stream<Mechanic> findBy(String name, String password) {
        return Stream.empty();
    }
}
