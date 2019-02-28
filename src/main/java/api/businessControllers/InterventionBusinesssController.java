package api.businessControllers;

import api.daos.DaoFactory;
import api.dtos.InterventionDto;
import api.entity.Intervention;
import api.entity.State;
import api.entity.Vehicle;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.NotFoundException;
import com.mysql.cj.core.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InterventionBusinesssController {

    public int create(InterventionDto interventionDto) {
        validateInterventionDto(interventionDto);

        Intervention intervention = new Intervention(interventionDto.getTitle(), interventionDto.getState(), interventionDto.getPeriod());

        if (!isCaffeIntervention(interventionDto)) {
            setVehicle(interventionDto, intervention);
        }

        if (intervention.getWork().isPresent()) {
            //Optional<Work> workOpt = DaoFactory.getFactory().getWorkDao().read(interventionDto.getId());
            //workOpt.ifPresent(work -> work.setWork(work));
            DaoFactory.getFactory().getInterventionDao().create(intervention);
        }

        DaoFactory.getFactory().getInterventionDao().create(intervention);
        return intervention.getId();
    }

    public static void setVehicle(InterventionDto interventionDto, Intervention intervention) {
        Vehicle vehicle = DaoFactory.getFactory().getVehicleDao().read(Integer.parseInt(interventionDto.getVehicleId()))
                .orElseThrow(() -> new ArgumentNotValidException("vehicle with id: " + interventionDto.getVehicleId() + "dont exists"));
        intervention.setVehicle(vehicle);
    }

    public void delete(String interventionId) {
        DaoFactory.getFactory().getInterventionDao().read(Integer.parseInt(interventionId))
                .orElseThrow(() -> new NotFoundException("Intervention id: " + interventionId));
        DaoFactory.getFactory().getInterventionDao().deleteById(Integer.parseInt(interventionId));
    }

    public static boolean isCaffeIntervention(InterventionDto interventionDto) {
        return (interventionDto.getVehicleId() == null || interventionDto.getVehicleId().isEmpty())
                && interventionDto.getState().equals(State.CAFFE);
    }

    private void validateInterventionDto(InterventionDto interventionDto) {
        if (interventionDto.getState().equals(State.CAFFE) && interventionDto.getVehicleId() != null) {
            throw new ArgumentNotValidException("Invalid intervention, CAFFE shouldnt have vehicle id: " + interventionDto.getVehicleId());
        }
        if (interventionDto.getState().equals(State.REPAIR) && interventionDto.getVehicleId() == null) {
            throw new ArgumentNotValidException("Invalid intervention, REPAIR should have vehicle id");
        }
    }

    public List<InterventionDto> readAll() {
        return DaoFactory.getFactory().getInterventionDao().findAll().map(InterventionDto::new).collect(Collectors.toList());
    }

    public InterventionDto read(String interventionId) {
        validateId(interventionId, "vehicle id");
        return DaoFactory.getFactory().getInterventionDao().read(Integer.parseInt(interventionId))
                .map(InterventionDto::new)
                .orElseThrow(() -> new NotFoundException("Intervention id: " + interventionId));
    }

    private void validateId(String id, String message) {
        validate(id, message);
        if ( !StringUtils.isStrictlyNumeric(id)){
            throw new NotFoundException(message +  " Should be numeric");
        }
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }
}
