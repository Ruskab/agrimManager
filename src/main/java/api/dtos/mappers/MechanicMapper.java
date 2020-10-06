package api.dtos.mappers;

import api.dtos.MechanicDto;
import api.entity.Intervention;
import api.entity.Mechanic;
import api.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Mapper
public interface MechanicMapper {

    MechanicMapper INSTANCE = Mappers.getMapper(MechanicMapper.class);

    @Mapping(target = "interventionIds", source = "interventionList")
    MechanicDto toMechanicDto(Mechanic mechanic);

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "interventionList", source = "interventionIds")
    Mechanic toMechanic(MechanicDto mechanicDto);

    default List<Integer> map(List<Intervention> interventions) {
        return interventions.stream().map(Intervention::getId).collect(toList());
    }

    default List<Intervention> mapInterventionsDto(List<Integer> interventions) {
        return new ArrayList<>();
    }

    default Set<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(Role::getValue).collect(Collectors.toSet());
    }

    default Set<Role> mapRolesDto(Set<String> roles) {
        return roles.stream().map(Role::getByValue).collect(Collectors.toSet());
    }

}