package api.apiControllers;

import api.businessControllers.ClientBusinessController;
import api.dtos.ClientDto;
import api.dtos.ClientVehiclesDto;
import api.dtos.VehicleDto;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.NotFoundException;
import com.mysql.cj.core.util.StringUtils;

import java.util.List;
import java.util.Optional;

public class ClientApiController {

    public static final String CLIENTS = "/clients";

    public static final String ID = "/{id}";

    public static final String ID_VEHICLES = ID + "/vehicles";

    private ClientBusinessController clientBusinessController = new ClientBusinessController();

    public int create(ClientDto clientDto) {
        this.validate(clientDto, "clientDto");
        this.validate(clientDto.getFullName(), "clientDto FullName");
        return clientBusinessController.create(clientDto);
    }

    public List<ClientDto> readAll() {
        return this.clientBusinessController.readAll();
    }

    public ClientDto read(String id) {
        this.validateId(id, "client id");
        return this.clientBusinessController.read(id);
    }

    public void update(String id, ClientDto clientDto) {
        this.validate(clientDto, "clientDto");
        this.validate(clientDto.getFullName(), "clientDto FullName");
        this.validateId(id, "client id: ");
        this.clientBusinessController.update(id, clientDto);
    }

    public void delete(String id) {
        this.validateId(id, "client id: ");
        this.clientBusinessController.delete(id);
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        if (!StringUtils.isStrictlyNumeric(id)) {
            throw new NotFoundException(message + " Should be numeric");
            //todo change to BadRequestException
        }
    }

    public ClientVehiclesDto clientVehiclesList(String clientId) {
        validateId(clientId, "client Id");
        return clientBusinessController.readClientVehicles(Integer.parseInt(clientId))
                .orElseThrow(() -> new NotFoundException("client with id: " + clientId));
    }
}
