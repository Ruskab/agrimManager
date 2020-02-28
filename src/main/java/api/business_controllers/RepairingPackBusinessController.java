package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.DaoSupplier;
import api.dtos.RepairingPackDto;
import api.entity.Intervention;
import api.entity.RepairingPack;
import api.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class RepairingPackBusinessController {

    static {
        DaoFactory.setFactory(DaoSupplier.HIBERNATE.createFactory());
    }

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
                .orElseThrow(() -> NotFoundException.throwBecauseOf("Repairing pack not found"));
    }

    public void updateReparingPack(String interventionId, String repairingPackId) {
        Intervention intervention = DaoFactory.getFactory().getInterventionDao().read(Integer.parseInt(interventionId)).orElseThrow(() -> NotFoundException.throwBecauseOf("Intervention not found"));
        RepairingPack repairingPack = DaoFactory.getFactory().getRepairingPackDao().read(Integer.parseInt(repairingPackId)).orElseThrow(() -> NotFoundException.throwBecauseOf("Repairing pack not found"));
        intervention.setRepairingPack(repairingPack);
        DaoFactory.getFactory().getInterventionDao().update(intervention);
    }

    public void delete(Integer id) {
        RepairingPack repairingPack = DaoFactory.getFactory().getRepairingPackDao().read((id))
                .orElseThrow(() -> NotFoundException.throwBecauseOf("" + id));
        DaoFactory.getFactory().getRepairingPackDao().deleteById(repairingPack.getId());
    }
}
