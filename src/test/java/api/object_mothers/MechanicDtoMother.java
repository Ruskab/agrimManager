package api.object_mothers;

import api.dtos.MechanicDto;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

public class MechanicDtoMother {

    public static final String FAKE_NAME = "fakeName";
    public static final String FAKE_PASSWORD = "fakePassword";

    public static MechanicDto create(String name, String password, List<Integer> interventionIds) {
        return new MechanicDto(name, password, interventionIds);
    }

    public static MechanicDto mechanicDto() {
        MechanicDto mechanicDto = create(FAKE_NAME, FAKE_PASSWORD, emptyList());
        mechanicDto.setRoles(Set.of("mechanic"));
        return mechanicDto;
    }

    public static MechanicDto withName(String name) {
        return create(name, FAKE_PASSWORD, emptyList());
    }

    public static MechanicDto withPassword(String password) {
        return create(FAKE_NAME, password, emptyList());
    }


    public static MechanicDto withInterventions(List<Integer> interventions) {
        return create(FAKE_NAME, FAKE_PASSWORD, interventions);
    }

    public static MechanicDto withRoles(List<String> roles) {
        MechanicDto mechanicDto = create(FAKE_NAME, FAKE_PASSWORD, emptyList());
        roles.forEach(mechanicDto::addRole);
        return mechanicDto;
    }

}
