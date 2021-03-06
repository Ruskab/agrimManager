package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.DaoSupplier;
import api.dtos.InterventionDto;
import api.dtos.mappers.InterventionMapper;
import api.entity.Intervention;
import api.entity.InterventionType;
import api.entity.Vehicle;
import api.exceptions.BadRequestException;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import com.mysql.cj.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class InterventionBusinesssController {

    static {
        DaoFactory.setFactory(DaoSupplier.HIBERNATE.createFactory());
    }

    static void setVehicle(InterventionDto interventionDto, Intervention intervention) {
        Vehicle vehicle = DaoFactory.getFactory().getVehicleDao().read(Integer.parseInt(interventionDto.getVehicleId()))
                .orElseThrow(() -> new BadRequestException("vehicle with id: " + interventionDto.getVehicleId() + "dont exists"));
        intervention.setVehicle(vehicle);
    }

    static boolean isCaffeIntervention(Intervention intervention) {
        return intervention.getInterventionType().equals(InterventionType.CAFFE);
    }

    public int create(InterventionDto interventionDto) {
        validateInterventionDto(interventionDto);
        Intervention intervention = new Intervention(interventionDto.getTitle(), InterventionType.valueOf(interventionDto.getInterventionType()), interventionDto
                .getStartTime(), interventionDto.getEndTime());
        if (!isCaffeIntervention(intervention)) {
            setVehicle(interventionDto, intervention);
        }
        DaoFactory.getFactory().getInterventionDao().create(intervention);
        return intervention.getId();
    }

    public void delete(String interventionId) {
        Intervention intervention = DaoFactory.getFactory().getInterventionDao().read(Integer.parseInt(interventionId))
                .orElseThrow(() -> NotFoundException.throwBecauseOf("Intervention id: " + interventionId));
        DaoFactory.getFactory().getInterventionDao().deleteById(intervention.getId());
    }

    private void validateInterventionDto(InterventionDto interventionDto) {
        if (interventionDto.getInterventionType()
                .equals("CAFFE") && interventionDto.getVehicleId() != null) {
            throw new BadRequestException("Invalid intervention, CAFFE shouldnt have vehicle id: " + interventionDto.getVehicleId());
        }
        if (interventionDto.getInterventionType()
                .equals("REPAIR") && interventionDto.getVehicleId() == null) {
            throw new BadRequestException("Invalid intervention, REPAIR should have vehicle id");
        }
    }

    public List<InterventionDto> readAll() {
        return DaoFactory.getFactory()
                .getInterventionDao()
                .findAll()
                .map(InterventionMapper.INSTANCE::toInterventionDto)
                .collect(Collectors.toList());
    }

    public InterventionDto read(String interventionId) {
        validateId(interventionId, "vehicle id");
        return DaoFactory.getFactory().getInterventionDao().read(Integer.parseInt(interventionId))
                .map(InterventionMapper.INSTANCE::toInterventionDto)
                .orElseThrow(() -> NotFoundException.throwBecauseOf("Intervention id: " + interventionId));
    }

    public void update(String id, InterventionDto interventionDto) {
        Intervention intervention = DaoFactory.getFactory().getInterventionDao().read((Integer.parseInt(id)))
                .orElseThrow(() -> NotFoundException.throwBecauseOf("Intervention ID" + id));

        intervention.setEndTime(interventionDto.getEndTime());
        intervention.setTitle(interventionDto.getTitle());
        DaoFactory.getFactory().getInterventionDao().update(intervention);
    }

    private void validateId(String id, String message) {
        validate(id, message);
        if (!StringUtils.isStrictlyNumeric(id)) {
            throw NotFoundException.throwBecauseOf(message + " Should be numeric");
        }
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new FieldInvalidException(message + " is missing");
        }
    }
}
