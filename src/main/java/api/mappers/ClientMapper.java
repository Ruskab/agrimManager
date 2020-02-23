package api.mappers;

import api.dtos.ClientDto;
import api.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDto toClientDto(Client client);

    Client toClient(ClientDto clientDto);

    void updateFromDto(ClientDto clientDto, @MappingTarget Client client);
}
