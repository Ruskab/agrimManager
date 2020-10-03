package api.object_mothers;

import front.dtos.Mechanic;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

public class FrontMechanicMother {

    public static final String FAKE_NAME = "fakeName";
    public static final String FAKE_PASSWORD = "fakePassword";

    public static Mechanic create(String name, String password, List<Integer> interventionIds) {
        return Mechanic.builder().name(name).password(password).roles(Set.of("mechanic")).interventionIds(interventionIds).build();
    }

    public static Mechanic mechanic() {
        Mechanic mechanicDto = create(FAKE_NAME, FAKE_PASSWORD, emptyList());
        mechanicDto.setRoles(Set.of("mechanic"));
        return mechanicDto;
    }

    public static Mechanic withName(String name) {
        return create(name, FAKE_PASSWORD, emptyList());
    }

    public static Mechanic withPassword(String password) {
        return create(FAKE_NAME, password, emptyList());
    }


    public static Mechanic withInterventions(List<Integer> interventions) {
        return create(FAKE_NAME, FAKE_PASSWORD, interventions);
    }

    public static Mechanic withRoles(List<String> roles) {
        Mechanic mechanic = create(FAKE_NAME, FAKE_PASSWORD, emptyList());
        roles.forEach(mechanic::addRole);
        return mechanic;
    }

}
