package api;

import api.entity.Client;
import api.entity.Intervention;
import api.entity.Mechanic;
import api.entity.Role;

import java.util.HashSet;
import java.util.List;

import static java.util.Collections.emptyList;

public class ClientMother {

    public static final String FAKE_FULL_NAME = "fakeFullName";
    public static final int HOURS = 10;

    public static Client create(String fullName, int hours) {
        return  Client.create(fullName, hours);
    }

    public static Client client() {
        return create(FAKE_FULL_NAME, HOURS);
    }

    public static Client withFullName(String fullName) {
        return create(fullName, HOURS);
    }

    public static Client withHours(Integer hours) {
        return create(FAKE_FULL_NAME, hours);
    }

}
