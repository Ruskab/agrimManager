package api.business_controllers;

import api.daos.DaoFactory;
import api.data_services.InterventionDataService;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.entity.Intervention;
import api.entity.Mechanic;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MechanicBusinessController {

    private static final String MECHANIC_ID = "Mechanic id: ";
    private InterventionBusinesssController interventionBO = new InterventionBusinesssController();

    public int create(MechanicDto mechanicDto) {
        this.validate(mechanicDto, "mechanicDto");
        this.validate(mechanicDto.getName(), "mechanicDto Name");
        this.validate(mechanicDto.getPassword(), "mechanicDto Password");
        Mechanic mechanic = new Mechanic(mechanicDto);
        List<InterventionDto> interventionDtos = new ArrayList<>();
        mechanicDto.getInterventionIds().forEach(id -> interventionDtos.add(interventionBO.read(Integer.toString(id))));
        mechanic.setInterventionList(interventionDtos.stream().map(this::createIntervention).collect(Collectors.toList()));
        DaoFactory.getFactory().getMechanicDao().create(mechanic);
        return mechanic.getId();
    }

    private Intervention createIntervention(InterventionDto interventionDto) {
        return new InterventionDataService().createIntervention(interventionDto);
    }

    private void validate(Object property, String message) {
        if (property == null || property.toString().equals("")) {
            throw new FieldInvalidException(message + " is missing");
        }
    }

    public void createIntervention(String mechanicId, InterventionDto interventionDto) {
        Mechanic mechanic = DaoFactory.getFactory().getMechanicDao().read(Integer.parseInt(mechanicId))
                .orElseThrow(() -> new NotFoundException("Mechanic not found"));

        Intervention intervention = new Intervention(interventionDto.getTitle(), interventionDto.getInterventionType(), interventionDto.getStartTime(), interventionDto.getEndTime());

        if (!InterventionBusinesssController.isCaffeIntervention(interventionDto)) {
            InterventionBusinesssController.setVehicle(interventionDto, intervention);
        }
        mechanic.getInterventionList().add(intervention);
        DaoFactory.getFactory().getMechanicDao().update(mechanic);
    }

    public List<MechanicDto> readAll() {
        return DaoFactory.getFactory().getMechanicDao().findAll().map(MechanicDto::new).collect(Collectors.toList());
    }

    public MechanicDto read(String id) {
        return DaoFactory.getFactory().getMechanicDao().read(Integer.parseInt(id)).map(MechanicDto::new)
                .orElseThrow(() -> new NotFoundException(MECHANIC_ID + id));
    }

    public List<MechanicDto> findBy(String name) {
        return DaoFactory.getFactory().getMechanicDao().findBy(name).map(MechanicDto::new).collect(Collectors.toList());
    }

    public void delete(String id) {
        Mechanic mechanic = DaoFactory.getFactory().getMechanicDao().read((Integer.parseInt(id)))
                .orElseThrow(() -> new NotFoundException(MECHANIC_ID + id));

        DaoFactory.getFactory().getMechanicDao().deleteById(mechanic.getId());
    }
}
