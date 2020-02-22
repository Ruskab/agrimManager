package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.DaoSupplier;
import api.dtos.ClientDto;
import api.dtos.ClientVehiclesDto;
import api.entity.Client;
import api.entity.Vehicle;
import api.exceptions.NotFoundException;
import api.mappers.ClientMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientBusinessController {

    private static final String CLIENT_ID = "Client id: ";

    static {
        DaoFactory.setFactory(DaoSupplier.HIBERNATE.createFactory());
    }

    private DaoFactory daoFactory = DaoFactory.getFactory();


    public ClientBusinessController() {
        //CDI
    }

    public int create(ClientDto clientDto) {
        Client client = Client.create(clientDto.getFullName(), clientDto.getHours());
        daoFactory.getClientDao().create(client);
        return client.getId();
    }

    public List<ClientDto> readAll() {
        return daoFactory.getClientDao().findAll()
                .map(ClientMapper.INSTANCE::toClientDto)
                .collect(Collectors.toList());
    }


    public ClientDto read(String id) {
        return daoFactory.getClientDao().read(Integer.parseInt(id)).map(ClientMapper.INSTANCE::toClientDto)
                .orElseThrow(() -> NotFoundException.throwBecauseOf(CLIENT_ID + id));
    }

    public void update(String id, ClientDto clientDto) {
        Client client = daoFactory.getClientDao().read((Integer.parseInt(id)))
                .orElseThrow(() -> NotFoundException.throwBecauseOf(CLIENT_ID + id));

        client.setFullName(clientDto.getFullName());
        client.setHours(clientDto.getHours());
        daoFactory.getClientDao().update(client);
    }

    public void delete(String id) {
        Client client = daoFactory.getClientDao().read((Integer.parseInt(id)))
                .orElseThrow(() -> NotFoundException.throwBecauseOf(CLIENT_ID + id));

        daoFactory.getClientDao().deleteById(client.getId());
    }

    public Optional<ClientVehiclesDto> readClientVehicles(int clientId) {
        if (existsClient(clientId)) {
            List<Integer> vehicleIds = getVehiclesIds(clientId);
            return Optional.of(new ClientVehiclesDto(read(Integer.toString(clientId)), vehicleIds));
        }
        return Optional.empty();
    }

    private List<Integer> getVehiclesIds(int clientId) {
        Client client = daoFactory.getClientDao().read(clientId).orElseThrow(() -> NotFoundException.throwBecauseOf("Client Not Found"));
        return daoFactory.getVehicleDao().findByClient(client).map(Vehicle::getId).collect(Collectors.toList());
    }

    private boolean existsClient(int clientId) {
        return daoFactory.getClientDao().read(clientId).isPresent();
    }
}
