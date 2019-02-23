package api.businessControllers;

import api.daos.DaoFactory;
import api.dtos.InterventionDto;
import api.entity.Intervention;
import api.entity.State;
import api.entity.Vehicle;
import api.exceptions.ArgumentNotValidException;

import java.util.Optional;

public class InterventionBusinesssController {

    public int create(InterventionDto interventionDto) {
        validateInterventionDto(interventionDto);

        Intervention intervention = new Intervention(interventionDto.getTitle(), interventionDto.getState(), interventionDto.getPeriod());

        if (!isCaffeIntervention(interventionDto)) {
            Optional<Vehicle> vehicleOpt = DaoFactory.getFactory().getVehicleDao().read(Integer.parseInt(interventionDto.getVehicleId()));
            vehicleOpt.ifPresent(vehicle -> intervention.setVehicle(vehicle));
            vehicleOpt.orElseThrow(() -> new ArgumentNotValidException("vehicle with id: " + interventionDto.getVehicleId() + "dont exists"));
        }

        if (intervention.getWork() != null) {
            //Optional<Work> workOpt = DaoFactory.getFactory().getWorkDao().read(interventionDto.getId());
            //workOpt.ifPresent(work -> work.setWork(work));
            DaoFactory.getFactory().getInterventionDao().create(intervention);
        }

        DaoFactory.getFactory().getInterventionDao().create(intervention);
        return intervention.getId();
    }

    private boolean isCaffeIntervention(InterventionDto interventionDto) {
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
}
