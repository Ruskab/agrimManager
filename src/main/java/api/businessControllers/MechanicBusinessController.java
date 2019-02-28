package api.businessControllers;

import api.daos.DaoFactory;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.entity.Intervention;
import api.entity.Mechanic;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.NotFoundException;

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
        if (property == null || property.toString().equals("")) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

    public void createIntervention(String mechanicId, InterventionDto interventionDto) {
        Mechanic mechanic = DaoFactory.getFactory().getMechanicDao().read(Integer.parseInt(mechanicId))
                    .orElseThrow(() -> new NotFoundException("Mechanic not found"));

        Intervention intervention = new Intervention(interventionDto.getTitle(), interventionDto.getState(), interventionDto.getPeriod());

        if (!InterventionBusinesssController.isCaffeIntervention(interventionDto)) {
            InterventionBusinesssController.setVehicle(interventionDto, intervention);
        }
        mechanic.getInterventionList().add(intervention);
        DaoFactory.getFactory().getMechanicDao().update(mechanic);
        intervention.getId();
    }
}
