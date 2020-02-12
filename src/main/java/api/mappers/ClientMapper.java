package api.mappers;

import api.dtos.ClientDto;
import api.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDto clientToClientDto(Client client);
}
