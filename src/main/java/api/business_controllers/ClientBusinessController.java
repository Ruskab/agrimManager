package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.DaoSupplier;
import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import api.dtos.mappers.ClientMapper;
import api.dtos.mappers.VehicleMapper;
import api.entity.Client;
import api.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class ClientBusinessController {

    private static final String CLIENT_ID = "Client id: ";

    static {
        DaoFactory.setFactory(DaoSupplier.HIBERNATE.createFactory());
    }

    private final ClientMapper clientMapper = ClientMapper.INSTANCE;


    public ClientBusinessController() {
        //CDI
    }

    public int create(ClientDto clientDto) {
        Client client = clientMapper.toClient(clientDto);
        DaoFactory.getFactory().getClientDao().create(client);
        return client.getId();
    }

    public List<ClientDto> readAll() {
        return DaoFactory.getFactory().getClientDao()
                .findAll()
                .map(clientMapper::toClientDto)
                .collect(toList());
    }


    public ClientDto read(String id) {
        return DaoFactory.getFactory().getClientDao()
                .read(Integer.parseInt(id))
                .map(clientMapper::toClientDto)
                .orElseThrow(() -> NotFoundException.throwBecauseOf(CLIENT_ID + id));
    }

    public void update(String id, ClientDto clientDto) {
        BiConsumer<ClientDto, Client> mapFromDto = clientMapper::updateFromDto;
        Consumer<Client> updateEntity = DaoFactory.getFactory().getClientDao()::update;
        DaoFactory.getFactory().getClientDao()
                .read((Integer.parseInt(id)))
                .ifPresentOrElse(
                        clt -> {
                            mapFromDto.accept(clientDto, clt);
                            updateEntity.accept(clt);
                        },
                        () -> NotFoundException.throwBecauseOf(CLIENT_ID + id));
    }

    public void delete(String id) {
        DaoFactory.getFactory().getClientDao()
                .read((Integer.parseInt(id)))
                .map(Client::getId)
                .ifPresentOrElse(
                        DaoFactory.getFactory().getClientDao()::deleteById,
                        () -> NotFoundException.throwBecauseOf(CLIENT_ID + id));
    }

    public List<ClientDto> searchByFullName(String fullName) {
        return DaoFactory.getFactory().getClientDao()
                .findAll()
                .filter(client -> client.getFullName().toLowerCase().contains(fullName.toLowerCase()))
                .map(clientMapper::toClientDto)
                .collect(toList());
    }

    public List<VehicleDto> searchClientVehicles(String clientId) {
        Optional<Client> optClient = DaoFactory.getFactory().getClientDao().read(Integer.parseInt(clientId));
        return optClient.map(this::findVehiclesByClient)
                .orElseThrow(() -> NotFoundException.throwBecauseOf("Client not found"));
    }

    private List<VehicleDto> findVehiclesByClient(Client client) {
        return DaoFactory.getFactory().getVehicleDao().findByClient(client).map(VehicleMapper.INSTANCE::toVehicleDto).collect(toList());
    }
}
