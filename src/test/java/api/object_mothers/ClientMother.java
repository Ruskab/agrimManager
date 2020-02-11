package api.object_mothers;

import api.entity.Client;

public class ClientMother {

    public static final String FAKE_FULL_NAME = "fakeFullName";
    public static final int HOURS = 10;

    public static Client create(String fullName, int hours) {
        return Client.create(fullName, hours);
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
