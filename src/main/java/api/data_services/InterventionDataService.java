package api.data_services;

import api.daos.DaoFactory;
import api.dtos.InterventionDto;
import api.entity.Intervention;
import api.entity.RepairingPack;
import api.entity.Vehicle;
import api.exceptions.NotFoundException;

public class InterventionDataService {

    private static final String VEHICLE_ID_MSG = "Vehicle id: ";
    private static final String REPAIRING_PACK_ID_MSG = "RepairingPack id: ";

    public Intervention createIntervention(InterventionDto interventionDto) {
        Intervention intervention = new Intervention();
        intervention.setRepairingPack(readRepairingPack(interventionDto.getRepairingPackId()));
        intervention.setVehicle(readVehicle(interventionDto.getVehicleId()));
        intervention.setEndTime(interventionDto.getEndTime());
        intervention.setStartTime(interventionDto.getStartTime());
        intervention.setInterventionType(interventionDto.getInterventionType());
        intervention.setTitle(interventionDto.getTitle());
        return intervention;
    }

    private Vehicle readVehicle(String id) {
        return DaoFactory.getFactory().getVehicleDao().read(Integer.parseInt(id))
                .orElseThrow(() -> new NotFoundException(VEHICLE_ID_MSG + id));
    }

    private RepairingPack readRepairingPack(String id) {
        return DaoFactory.getFactory().getRepairingPackDao().read(Integer.parseInt(id))
                .orElseThrow(() -> new NotFoundException(REPAIRING_PACK_ID_MSG + id));
    }
}
