package api.entity;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMIN("admin"),
    MECHANIC("mechanic"),
    DEVELOPER("developer");

    private String value;

    public boolean isSuperUser() {
        return List.of(ADMIN, DEVELOPER).contains(this);
    }

    public static Role getByValue(String role) {
        return Arrays.stream(values())
                .filter(r -> role.equals(r.value)).findFirst()
                .orElseThrow(NullPointerException::new);
    }

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
