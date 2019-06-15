package client_beans.util.converters;

import api.entity.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MechanicRoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role.getValue();
    }

    @Override
    public Role convertToEntityAttribute(String s) {
        switch (s) {
            case "admin":
                return Role.ADMIN;
            case "mechanic":
                return Role.MECHANIC;
            case "developer":
                return Role.DEVELOPER;
            default:
                return Role.MECHANIC;
        }

    }
}
