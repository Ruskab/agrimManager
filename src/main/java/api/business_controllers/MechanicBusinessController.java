package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.DaoSupplier;
import api.data_services.InterventionDataService;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.mappers.InterventionMapper;
import api.dtos.mappers.MechanicMapper;
import api.entity.Intervention;
import api.entity.InterventionType;
import api.entity.Mechanic;
import api.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class MechanicBusinessController {

    private static final String MECHANIC_ID = "Mechanic id: ";

    static {
        DaoFactory.setFactory(DaoSupplier.HIBERNATE.createFactory());
    }

    private final InterventionBusinesssController interventionBO = new InterventionBusinesssController();

    public int create(MechanicDto mechanicDto) {
        Mechanic mechanic = MechanicMapper.INSTANCE.toMechanic(mechanicDto);
        List<InterventionDto> interventionDtos = new ArrayList<>();
        mechanicDto.getInterventionIds().forEach(id -> interventionDtos.add(interventionBO.read(Integer.toString(id))));
        mechanic.setInterventionList(interventionDtos.stream()
                .map(this::createIntervention)
                .collect(toList()));
        DaoFactory.getFactory().getMechanicDao().create(mechanic);
        return mechanic.getId();
    }

    private Intervention createIntervention(InterventionDto interventionDto) {
        return new InterventionDataService().createIntervention(interventionDto);
    }

    public void createIntervention(String mechanicId, InterventionDto interventionDto) {
        Mechanic mechanic = DaoFactory.getFactory().getMechanicDao().read(Integer.parseInt(mechanicId))
                .orElseThrow(() -> NotFoundException.throwBecauseOf("Mechanic not found"));

        Intervention intervention = new Intervention(interventionDto.getTitle(), InterventionType.valueOf(interventionDto
                .getInterventionType()), interventionDto
                .getStartTime(), interventionDto.getEndTime());

        if (!InterventionBusinesssController.isCaffeIntervention(intervention)) {
            InterventionBusinesssController.setVehicle(interventionDto, intervention);
        }
        mechanic.getInterventionList().add(intervention);
        DaoFactory.getFactory().getMechanicDao().update(mechanic);
    }

    public List<MechanicDto> readAll() {
        return DaoFactory.getFactory()
                .getMechanicDao()
                .findAll()
                .map(MechanicMapper.INSTANCE::toMechanicDto)
                .collect(toList());
    }

    public MechanicDto read(String id) {
        return DaoFactory.getFactory()
                .getMechanicDao()
                .read(Integer.parseInt(id))
                .map(MechanicMapper.INSTANCE::toMechanicDto)
                .orElseThrow(() -> NotFoundException.throwBecauseOf(MECHANIC_ID + id));
    }

    public List<MechanicDto> searchBy(String username) {
        return DaoFactory.getFactory()
                .getMechanicDao()
                .findBy(username)
                .map(MechanicMapper.INSTANCE::toMechanicDto)
                .collect(toList());
    }

    public List<MechanicDto> searchBy(String username, String password) {
        return DaoFactory.getFactory()
                .getMechanicDao()
                .findBy(username, password)
                .map(MechanicMapper.INSTANCE::toMechanicDto)
                .collect(toList());
    }

    public List<InterventionDto> getMechanicInterventions(Integer mechanicId, Boolean isActive) {
        return DaoFactory.getFactory().getMechanicDao().read(mechanicId)
                .stream().flatMap(mechanic -> mechanic.getInterventionList().stream())
                .filter(intervention -> intervention.isActive() == isActive)
                .map(InterventionMapper.INSTANCE::toInterventionDto)
                .collect(toList());
    }


    public void delete(String id) {
        Mechanic mechanic = DaoFactory.getFactory().getMechanicDao().read((Integer.parseInt(id)))
                .orElseThrow(() -> NotFoundException.throwBecauseOf(MECHANIC_ID + id));

        DaoFactory.getFactory().getMechanicDao().deleteById(mechanic.getId());
    }

    public void finishIntervention(Integer id, String interventionId) {
        Optional<Mechanic> optMechanic = DaoFactory.getFactory().getMechanicDao().read(id);
        if (optMechanic.isPresent()) {
            List<Intervention> interventions = optMechanic.get().getInterventionList();
            interventions.stream().filter(intervention -> interventionId.equals(Integer.toString(intervention.getId())))
                    .findFirst().ifPresent(intervention -> intervention.setEndTime(LocalDateTime.now()));
        }
    }

}
