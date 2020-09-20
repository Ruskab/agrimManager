package api.dtos.mappers;

import api.dtos.VehicleDto;
import api.entity.Client;
import api.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface VehicleMapper {

    VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

    @Mapping(target = "clientId", source = "client")
    VehicleDto toVehicleDto(Vehicle vehicle);

    default String mapClientId(Client client) {
        return Optional.ofNullable(client)
                .map(Client::getId)
                .map(Object::toString)
                .orElse(null);
    }


}
