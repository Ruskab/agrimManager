package api.businessControllers;

import api.daos.DaoFactory;
import api.dtos.MechanicDto;
import api.entity.Mechanic;
import api.exceptions.ArgumentNotValidException;

public class MechanicBusinessController {

    public int create(MechanicDto mechanicDto) {
        this.validate(mechanicDto, "mechanicDto");
        this.validate(mechanicDto.getName(), "mechanicDto Name");
        this.validate(mechanicDto.getPassword(), "mechanicDto Password");
        Mechanic mechanic = new Mechanic(mechanicDto.getName(), mechanicDto.getPassword());
        DaoFactory.getFactory().getMechanicDao().create(mechanic);
        return mechanic.getId();
    }

    private void validate(Object property, String message) {
        if (property == null || property.toString() == "") {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

}
