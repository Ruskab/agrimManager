package api.api_controllers;

import api.business_controllers.RepairingPackBusinessController;
import api.dtos.RepairingPackDto;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.RequestInvalidException;
import com.mysql.cj.util.StringUtils;

import java.util.List;

public class RepairingPackApiController {
    public static final String REPAIRING_PACKS = "/repairing-packs";

    public static final String ID = "/{id}";

    private RepairingPackBusinessController repairingPackBusinessController  = new RepairingPackBusinessController();

    public int create(RepairingPackDto repairingPackDto) {
        this.validate(repairingPackDto, "repairingPackDto");
        this.validate(repairingPackDto.getInvoicedHours(), "Invoiced hours");
        this.validate(repairingPackDto.getInvoicedDate(), "Invoiced Date");
        return this.repairingPackBusinessController.create(repairingPackDto);
    }

    public RepairingPackDto read(String id) {
        this.validateId(id, "RepairingPack id");
        return this.repairingPackBusinessController.read(id);
    }

    public List<RepairingPackDto> readAll() {
        return this.repairingPackBusinessController.readAll();
    }

    public void updateReparingPack(String interventionId, String repairingPackId) {
        this.validateId(interventionId, "intervention id");
        this.validateId(repairingPackId, "repairing pack id");
        this.repairingPackBusinessController.updateReparingPack(interventionId, repairingPackId);
    }

    public void validate(Object property, String message) {
        if (property == null || property.toString().equals("")) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        if (!StringUtils.isStrictlyNumeric(id)) {
            throw new RequestInvalidException(message + " Should be numeric");
        }
    }
}
