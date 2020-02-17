package api.mappers;

import api.dtos.MechanicDto;
import api.entity.Intervention;
import api.entity.Mechanic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Mapper
public interface MechanicMapper {

    MechanicMapper INSTANCE = Mappers.getMapper(MechanicMapper.class);

    @Mapping(target = "interventionIds", source = "interventionList")
    MechanicDto toMechanicDto(Mechanic mechanic);

    default List<Integer> map(List<Intervention> interventions) {
        return interventions.stream().map(Intervention::getId).collect(toList());
    }

}
