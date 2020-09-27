package api.dtos.mappers;

import api.dtos.InterventionDto;
import api.entity.Intervention;
import api.entity.InterventionType;
import api.entity.RepairingPack;
import api.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface InterventionMapper {

    InterventionMapper INSTANCE = Mappers.getMapper(InterventionMapper.class);

    @Mapping(target = "vehicleId", source = "vehicle")
    @Mapping(target = "repairingPackId", source = "repairingPack")
    InterventionDto toInterventionDto(Intervention intervention);

    default String mapVehicleId(Optional<Vehicle> vehicle) {
        return vehicle
                .map(Vehicle::getId)
                .map(id -> Integer.toString(id))
                .orElse(null);
    }

    default String mapRepairingPackId(Optional<RepairingPack> repairingPack) {
        return repairingPack
                .map(RepairingPack::getId)
                .map(id -> Integer.toString(id))
                .orElse(null);
    }

    default String mapType(InterventionType type) {
        return type.name();
    }

}
