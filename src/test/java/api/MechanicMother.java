package api;

import api.entity.Intervention;
import api.entity.Mechanic;
import api.entity.Role;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class MechanicMother {

    public static final String FAKE_NAME = "fakeName";
    public static final String FAKE_PASSWORD = "fakePassword";

    public static Mechanic create(String name, String password, List<Intervention> interventions) {
        return new Mechanic(name, password, interventions);
    }

    public static Mechanic mechanic() {
        return create(FAKE_NAME, FAKE_PASSWORD, emptyList());
    }

    public static Mechanic withName(String name) {
        return create(name, FAKE_PASSWORD, emptyList());
    }

    public static Mechanic withPassword(String password) {
        return create(FAKE_NAME, password, emptyList());
    }

    public static Mechanic withInterventions(List<Intervention> interventions) {
        return create(FAKE_NAME, FAKE_PASSWORD, interventions);
    }

    public static Mechanic withRoles(List<Role> roles) {
        Mechanic mechanic = create(FAKE_NAME, FAKE_PASSWORD, emptyList());
        mechanic.setRoles(new HashSet<>(roles));
        return mechanic;
    }

}
