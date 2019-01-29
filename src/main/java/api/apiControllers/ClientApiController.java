package api.apiControllers;

import api.businessControllers.ClientBusinessController;
import api.dtos.ClientDto;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.NotFoundException;
import com.mysql.cj.core.util.StringUtils;

import java.util.List;

public class ClientApiController {

    public static final String CLIENTS = "/clients";

    public static final String ID_ID = "/{id}";

    private ClientBusinessController clientBusinessController = new ClientBusinessController();

    public int create(ClientDto clientDto){
        this.validate(clientDto, "clientDto");
        this.validate(clientDto.getFullName(), "clientDto FullName");
        return clientBusinessController.create(clientDto);
    }

    public List<ClientDto> readAll() {
        return this.clientBusinessController.readAll();
    }

    public void update(String id,ClientDto clientDto) {
        this.validate(clientDto, "clientDto");
        this.validate(clientDto.getFullName(), "clientDto FullName");
        this.validateId(id, "client id: ");
        this.clientBusinessController.update(id,clientDto);
    }

    private void validateId(String id, String message) {
        if (!StringUtils.isStrictlyNumeric(id)){
            throw new NotFoundException(message +  " Should be numeric");
        }
    }

    public void delete(String id) {
        this.validateId(id, "client id: ");
        this.clientBusinessController.delete(id);
    }

    private void validate(Object property, String message) {
        if (property == null){
            throw new ArgumentNotValidException(message + " is missing");
        }
    }
}