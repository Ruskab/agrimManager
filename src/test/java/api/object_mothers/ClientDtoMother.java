package api.object_mothers;

import api.dtos.ClientDto;

public class ClientDtoMother {

    public static final String FAKE_FULL_NAME = "fakeFullName";
    public static final int HOURS = 10;

    public static ClientDto create(String fullName, int hours) {
        return ClientDto.builder().fullName(fullName).hours(hours).build();
    }

    public static ClientDto clientDto() {
        return create(FAKE_FULL_NAME, HOURS);
    }

    public static ClientDto withFullName(String fullName) {
        return create(fullName, HOURS);
    }

    public static ClientDto withHours(Integer hours) {
        return create(FAKE_FULL_NAME, hours);
    }

}
