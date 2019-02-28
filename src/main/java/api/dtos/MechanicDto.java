package api.dtos;

import api.entity.Intervention;
import api.entity.Mechanic;

import java.util.List;
import java.util.stream.Collectors;

public class MechanicDto {
    private int id;

    private String name;

    private String password;

    private List<Integer> interventionIds;

    public MechanicDto(String name, String password, List<Integer> interventionIds) {
        this.name = name;
        this.password = password;
        this.interventionIds = interventionIds;
    }

    public MechanicDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public MechanicDto(Mechanic mechanic) {
        this.id = mechanic.getId();
        this.name = mechanic.getName();
        this.password = mechanic.getPassword();
        this.interventionIds = mechanic.getInterventionList()
                .stream()
                .map(Intervention::getId)
                .collect(Collectors.toList());
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
}
