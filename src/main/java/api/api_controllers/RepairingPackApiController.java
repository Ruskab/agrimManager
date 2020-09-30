package api.api_controllers;

import api.business_controllers.RepairingPackBusinessController;
import api.dtos.RepairingPackDto;
import api.exceptions.FieldInvalidException;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(RepairingPackApiController.REPAIRING_PACKS)
@Api(value = RepairingPackApiController.REPAIRING_PACKS)
public class RepairingPackApiController {
    public static final String REPAIRING_PACKS = "/repairing-packs";

    public static final String ID = "/{id}";

    private RepairingPackBusinessController repairingPackBusinessController = new RepairingPackBusinessController();

    @POST
    @ApiOperation(value = "Create new RepairingPack")
    @Consumes(MediaType.APPLICATION_JSON)
    public int create(RepairingPackDto repairingPackDto) {
        this.validate(repairingPackDto, "repairingPackDto");
        this.validate(repairingPackDto.getInvoicedHours(), "Invoiced hours");
        this.validate(repairingPackDto.getInvoicedDate(), "Invoiced Date");
        return this.repairingPackBusinessController.create(repairingPackDto);
    }


    public RepairingPackDto getById(String id) {
        this.validateId(id, "RepairingPack id");
        return this.repairingPackBusinessController.read(id);
    }

    public List<RepairingPackDto> listAll() {
        return this.repairingPackBusinessController.readAll();
    }

    public void updateReparingPack(String interventionId, String repairingPackId) {
        this.validateId(interventionId, "intervention id");
        this.validateId(repairingPackId, "repairing pack id");
        this.repairingPackBusinessController.updateReparingPack(interventionId, repairingPackId);
    }

    public void validate(Object property, String message) {
        if (property == null || property.toString().equals("")) {
            throw new FieldInvalidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        if (!StringUtils.isStrictlyNumeric(id)) {
            throw new FieldInvalidException(message + " Should be numeric");
        }
    }
}
