package api.businessControllers;

import api.daos.DaoFactory;
import api.dtos.ClientDto;
import api.entity.Client;
import api.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class ClientBusinessController {

    public int create(ClientDto clientDto) {
        Client client = new Client(clientDto.getFullName(), clientDto.getHours());
        DaoFactory.getFactory().getClientDao().create(client);
        return client.getId();
    }

    public List<ClientDto> readAll() {
        return DaoFactory.getFactory().getClientDao().findAll().stream().map(ClientDto::new).collect(Collectors.toList());
    }


    public ClientDto read(String id) {
        return DaoFactory.getFactory().getClientDao().read(Integer.parseInt(id)).map(ClientDto::new)
                .orElseThrow(() -> new NotFoundException("Client id: " + id));
    }

    public void update(String id, ClientDto clientDto) {
        Client client = DaoFactory.getFactory().getClientDao().read((Integer.parseInt(id)))
                .orElseThrow(() -> new NotFoundException("Client id: " + id));

        client.setFullName(clientDto.getFullName());
        client.setHours(clientDto.getHours());
        DaoFactory.getFactory().getClientDao().update(client);
    }

    public void delete(String id) {
        DaoFactory.getFactory().getClientDao().read((Integer.parseInt(id)))
                .orElseThrow(() -> new NotFoundException("Client id: " + id));

        DaoFactory.getFactory().getClientDao().deleteById(Integer.parseInt(id));
    }
}
