package api.business_controllers;

import api.daos.DaoFactory;
import api.dtos.RepairingPackDto;
import api.entity.Intervention;
import api.entity.RepairingPack;
import api.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class RepairingPackBusinessController {
    public int create(RepairingPackDto repairingPackDto) {
        RepairingPack repairingPack = new RepairingPack(repairingPackDto.getInvoicedDate(), repairingPackDto.getInvoicedHours());
        DaoFactory.getFactory().getRepairingPackDao().create(repairingPack);
        return repairingPack.getId();
    }

    public List<RepairingPackDto> readAll() {
        return DaoFactory.getFactory().getRepairingPackDao().findAll().map(RepairingPackDto::new).collect(Collectors.toList());
    }

    public RepairingPackDto read(String id) {
        return DaoFactory.getFactory().getRepairingPackDao().read(Integer.parseInt(id)).map(RepairingPackDto::new)
                .orElseThrow(() -> new NotFoundException("Repairing pack not found"));
    }

    public void updateReparingPack(String interventionId, String repairingPackId) {
        Intervention intervention = DaoFactory.getFactory().getInterventionDao().read(Integer.parseInt(interventionId)).orElseThrow(() -> new NotFoundException("Intervention not found"));
        RepairingPack repairingPack = DaoFactory.getFactory().getRepairingPackDao().read(Integer.parseInt(repairingPackId)).orElseThrow(() -> new NotFoundException("Repairing pack not found"));
        intervention.setRepairingPack(repairingPack);
        DaoFactory.getFactory().getInterventionDao().update(intervention);
    }

    public void delete(Integer id) {
        RepairingPack repairingPack = DaoFactory.getFactory().getRepairingPackDao().read((id))
                .orElseThrow(() -> new NotFoundException("" + id));
        DaoFactory.getFactory().getRepairingPackDao().deleteById(repairingPack.getId());
    }
}
