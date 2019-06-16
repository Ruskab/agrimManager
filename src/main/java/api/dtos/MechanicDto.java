package api.dtos;

import api.entity.Intervention;
import api.entity.Mechanic;
import api.entity.Role;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MechanicDto implements Serializable {
    private int id;

    private String name;

    private String password;

    private List<Integer> interventionIds;

    private Set<Role> roles = new HashSet<>();

    public MechanicDto(String name, String password, List<Integer> interventionIds) {
        this.name = name;
        this.password = password;
        this.interventionIds = interventionIds;
    }

    public MechanicDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public MechanicDto() {
    }

    public MechanicDto(Mechanic mechanic) {
        this.id = mechanic.getId();
        this.name = mechanic.getName();
        this.password = mechanic.getPassword();
        this.interventionIds = mechanic.getInterventionList()
                .stream()
                .map(Intervention::getId)
                .collect(Collectors.toList());
        this.roles = mechanic.getRoles();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getInterventionIds() {
        return interventionIds;
    }

    public void setInterventionIds(List<Integer> interventionIds) {
        this.interventionIds = interventionIds;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public boolean hasAnyRole(List<Role> roles) {
        return roles.stream().anyMatch(roles::contains);
    }

    public void addRole(Role role) {
        if (!hasRole(role)) {
            roles.add(role);
        }
    }

    public boolean anyRoleGranted(Role... roles) {
        for (Role role : roles) {
            if (this.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

}
