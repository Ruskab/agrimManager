package api.entity;

public enum Role {
    ADMIN("admin"),
    MECHANIC("mechanic"),
    DEVELOPER("developer");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
