package api.mappers;

import api.dtos.RepairingPackDto;
import api.entity.RepairingPack;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RepairingPackMapper {

    RepairingPackMapper INSTANCE = Mappers.getMapper(RepairingPackMapper.class);

    RepairingPackDto toRepairingPackDto(RepairingPack repairingPack);

}
