package api.stub;

import api.daos.MechanicDao;
import api.entity.Mechanic;

import java.util.Map;
import java.util.stream.Stream;

class MechanicDaoStub extends GenericDaoStub<Mechanic, Integer> implements MechanicDao {

    public MechanicDaoStub(Map<Integer, Mechanic> entities) {
        super(entities);
    }

    @Override
    public Stream<Mechanic> findBy(String name) {
        return entities.values().stream().filter(mechanic -> name.equals(mechanic.getName()));
    }

    @Override
    public Stream<Mechanic> findBy(String name, String password) {
        return entities.values().stream()
                .filter(mechanic -> name.equals(mechanic.getName()))
                .filter(mechanic -> password.equals(mechanic.getPassword()));
    }
}
