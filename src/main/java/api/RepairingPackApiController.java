package api;

import api.businessControllers.RepairingPackBusinessController;
import api.dtos.RepairingPackDto;
import api.exceptions.ArgumentNotValidException;

public class RepairingPackApiController {
    public static final String REPAIRING_PACKS = "/repairing-packs";
    private RepairingPackBusinessController repairingPackBusinessController  = new RepairingPackBusinessController();

    public int create(RepairingPackDto repairingPackDto) {
        this.validate(repairingPackDto, "repairingPackDto");
        this.validate(repairingPackDto.getInvoicedHours(), "Invoiced hours");
        this.validate(repairingPackDto.getInvoicedDate(), "Invoiced Date");
        return this.repairingPackBusinessController.create(repairingPackDto);
    }

    public void validate(Object property, String message) {
        if (property == null || property.toString().equals("")) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

}
